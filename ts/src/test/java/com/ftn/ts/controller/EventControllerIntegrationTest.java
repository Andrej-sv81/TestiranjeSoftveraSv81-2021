package com.ftn.ts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.ts.dto.AgendaItemDTO;
import com.ftn.ts.dto.EventDTO;
import com.ftn.ts.model.Event;
import com.ftn.ts.model.EventType;
import com.ftn.ts.model.PrivacyType;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.repository.EventRepository;
import com.ftn.ts.repository.EventTypeRepository;
import com.ftn.ts.repository.UserODRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserODRepository userODRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private EventRepository eventRepository;

    private Long eventTypeId;
    private String userEmail;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        userODRepository.deleteAll();
        eventTypeRepository.deleteAll();

        userEmail = "andrej5@gmail.com";
        UserOD user = new UserOD();
        user.setEmail(userEmail);
        user.setPassword("andrej5");
        user.setName("Andrej");
        user.setSurname("Mitrovic");
        user.setAddress("Vuka Karadzica 10");
        user.setPhone("0654634633");
        userODRepository.save(user);

        EventType type = new EventType();
        type.setName("Conference");
        eventTypeId = eventTypeRepository.save(type).getId();
    }

    private Long createAndSaveEvent() {
        UserOD organizer = userODRepository.findByEmail(userEmail).orElseThrow();
        EventType type = eventTypeRepository.findById(eventTypeId).orElseThrow();
        Event e = new Event();
        e.setName("Event Name 1");
        e.setDescription("Description of the 1st event");
        e.setMaxParticipants(123);
        e.setPrivacyType(PrivacyType.OPEN);
        e.setLocation("Novi Sad");
        e.setEventDate(LocalDateTime.now().plusDays(1));
        e.setOrganizer(organizer);
        e.setEventType(type);
        return eventRepository.save(e).getId();
    }

    private EventDTO createEventDTO(Long eventTypeId) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Test Event Name 1");
        eventDTO.setDescription("Description of the test event");
        eventDTO.setEventTypeId(eventTypeId);
        eventDTO.setEventDate("2025-12-01T10:00");
        eventDTO.setLocation("Novi Sad, Location 1");
        eventDTO.setMaxParticipants(134);
        eventDTO.setPrivacyType("OPEN");
        return eventDTO;
    }

    private AgendaItemDTO createAgendaDTO() {
        AgendaItemDTO agendaItemDTO = new AgendaItemDTO();
        agendaItemDTO.setTitle("Agenda Item Name 1");
        agendaItemDTO.setDescription("Description of the agenda item 1");
        agendaItemDTO.setStartTime("2025-12-01T10:00");
        agendaItemDTO.setEndTime("2025-12-01T11:00");
        agendaItemDTO.setLocation("Conference Room A123");
        return agendaItemDTO;
    }
    
    
    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_returns_its_id_if_the_eventDTO_is_valid() throws Exception {
 
        EventDTO eventDTO = createEventDTO(eventTypeId);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    void Creating_an_event_returns_unauthorized_when_principal_is_missing() throws Exception {
        
        EventDTO eventDTO = createEventDTO(eventTypeId);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isUnauthorized());
    }

    
    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_returns_its_id_if_the_agendaDTO_is_valid() throws Exception {
        
        Long eventId = createAndSaveEvent();
        AgendaItemDTO agendaDTO = createAgendaDTO();

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_returns_bad_request_if_event_data_is_invalid() throws Exception {
        
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName(""); // ne smije biti prazno
        eventDTO.setDescription("Test Description");

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_returns_bad_request_if_item_data_is_invalid() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO agendaDTO = new AgendaItemDTO();
        agendaDTO.setTitle("");

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_returns_not_found_when_event_is_not_found() throws Exception {

        AgendaItemDTO agendaItemDTO = createAgendaDTO();

        mockMvc.perform(post("/api/events/{eventId}/agenda", 12312123L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendaItemDTO)))
                .andExpect(status().isNotFound());
    }



    // granice valicacije eventDTO - @Valid
    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_allows_minimum_maxParticipants_of_1() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setMaxParticipants(1);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_maxParticipants_below_minimum() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setMaxParticipants(0);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_allows_maximum_maxParticipants_of_10000() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setMaxParticipants(10000);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_maxParticipants_above_maximum() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setMaxParticipants(10001);

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_allows_name_of_max_length_150() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setName("A".repeat(150));

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_name_longer_than_150() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setName("A".repeat(151));
        
        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_empty_name() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setName("");
        
        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_invalid_privacy_type() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setPrivacyType("PUBLIC"); // OPEN|CLOSED

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_invalid_event_date_format() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setEventDate("12/31/2025 10:00"); // pattern yyyy-MM-dd'T'HH:mm

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_allows_location_at_max_length_255() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setLocation("L".repeat(255));
        
        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_location_longer_than_255() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setLocation("L".repeat(256));

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_empty_location() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setLocation("");

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_allows_description_of_max_length_255() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setDescription("D".repeat(255));

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_description_longer_than_255() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setDescription("D".repeat(256));

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Creating_an_event_doesnt_allow_empty_description() throws Exception {

        EventDTO dto = createEventDTO(eventTypeId);
        dto.setDescription("");

        mockMvc.perform(post("/api/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // granice valicacije agendaItemDTO - @Valid
    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_allows_title_at_max_length_100() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setTitle("T".repeat(100));

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_title_longer_than_100() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setTitle("T".repeat(101));

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_empty_title() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setTitle("");

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_invalid_start_time_format() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setStartTime("01-12-2025 10:00"); 
     
        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_invalid_end_time_format() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setEndTime("2025/12/01 11:00");

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_allows_location_at_max_length_255() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setLocation("L".repeat(255));

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.matchesRegex("\\d+")));
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_location_longer_than_255() throws Exception {
        
        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setLocation("L".repeat(256));

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_empty_location() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setLocation("");

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "andrej5@gmail.com", roles = "USER")
    void Adding_agenda_item_doesnt_allow_empty_description() throws Exception {

        Long eventId = createAndSaveEvent();
        AgendaItemDTO dto = createAgendaDTO();
        dto.setDescription("");

        mockMvc.perform(post("/api/events/{eventId}/agenda", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


}