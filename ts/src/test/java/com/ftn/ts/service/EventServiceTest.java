package com.ftn.ts.service;

import com.ftn.ts.dto.AgendaItemDTO;
import com.ftn.ts.dto.EventDTO;
import com.ftn.ts.dto.EventMapper;
import com.ftn.ts.model.*;
import com.ftn.ts.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventTypeRepository eventTypeRepository;
    @Mock
    private UserODRepository userODRepository;
    @Mock
    private EventAgendaRepository agendaRepository;

    @InjectMocks
    private EventService eventService;

    private UserOD user;
    private EventType eventType;
    private EventDTO eventDTO;
    private Event event;
    private AgendaItemDTO agendaItemDTO;
    private EventAgendaItem agendaItem;

    @BeforeEach
    void setUp() {
        user = new UserOD();
        user.setId(1L);
        user.setEmail("andrej5@gmail.com");
        user.setName("Andrej");
        user.setSurname("Mitrovic");

        eventType = new EventType();
        eventType.setId(1L);
        eventType.setName("Conference");

        eventDTO = new EventDTO();
        eventDTO.setName("Test Event");
        eventDTO.setDescription("Event Description");
        eventDTO.setMaxParticipants(123);
        eventDTO.setPrivacyType("OPEN");
        eventDTO.setLocation("Novi Sad");
        eventDTO.setEventDate("2024-12-25T10:00");
        eventDTO.setEventTypeId(1L);

        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDescription("Event Description");
        event.setMaxParticipants(123);
        event.setPrivacyType(PrivacyType.OPEN);
        event.setLocation("Novi Sad");
        event.setEventDate(LocalDateTime.of(2024, 12, 25, 10, 0));
        event.setOrganizer(user);
        event.setEventType(eventType);

        agendaItemDTO = new AgendaItemDTO();
        agendaItemDTO.setTitle("Opening Ceremony");
        agendaItemDTO.setDescription("Welcome and introduction description");
        agendaItemDTO.setStartTime("2024-12-25T10:00");
        agendaItemDTO.setEndTime("2024-12-25T11:00");
        agendaItemDTO.setLocation("Main Stage Location");

        agendaItem = new EventAgendaItem();
        agendaItem.setId(1L);
        agendaItem.setTitle("Opening Ceremony");
        agendaItem.setDescription("Welcome and introduction description");
        agendaItem.setStartTime(LocalDateTime.of(2024, 12, 25, 10, 0));
        agendaItem.setEndTime(LocalDateTime.of(2024, 12, 25, 11, 0));
        agendaItem.setLocation("Main Stage Location");
        agendaItem.setEvent(event);
    }

    //pravljenje dogadjaja
    @Test
    void Creating_an_event_returns_the_event_when_valid_data_is_provided() {

        when(userODRepository.findByEmail("andrej5@gmail.com")).thenReturn(Optional.of(user));
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.of(eventType));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        try (MockedStatic<EventMapper> eventMapperMock = mockStatic(EventMapper.class)) {
            
            eventMapperMock.when(() -> EventMapper.toEntity(eventDTO, user, eventType)).thenReturn(event);

            Event result = eventService.createEvent(eventDTO, "andrej5@gmail.com");

            assertNotNull(result);
            assertEquals(event.getId(), result.getId());
            assertEquals(event.getName(), result.getName());
            assertEquals(event.getDescription(), result.getDescription());
            assertEquals(event.getOrganizer(), result.getOrganizer());
            assertEquals(event.getEventType(), result.getEventType());

            verify(userODRepository).findByEmail("andrej5@gmail.com");
            verify(eventTypeRepository).findById(1L);
            verify(eventRepository).save(any(Event.class));
            eventMapperMock.verify(() -> EventMapper.toEntity(eventDTO, user, eventType));
        }
    }

    @Test
    void Event_creation_throws_exception_if_the_user_doesnt_exist() {

        when(userODRepository.findByEmail("andrej5@gmail.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> eventService.createEvent(eventDTO, "andrej5@gmail.com"));
        assertEquals("andrej5@gmail.com", exception.getMessage());

        verify(userODRepository).findByEmail("andrej5@gmail.com");
        verify(eventTypeRepository, never()).findById(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void Event_creation_throws_exception_when_user_email_is_null() {

        when(userODRepository.findByEmail(null)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> eventService.createEvent(eventDTO, null));
        assertNull(exception.getMessage());

        verify(userODRepository).findByEmail(null);
        verify(eventTypeRepository, never()).findById(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void Event_creation_throws_exception_when_user_email_is_empty() {
    
        when(userODRepository.findByEmail("")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> eventService.createEvent(eventDTO, ""));
        assertEquals("", exception.getMessage());

        verify(userODRepository).findByEmail("");
        verify(eventTypeRepository, never()).findById(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void Event_creation_throws_exception_when_event_type_is_not_found() {
        when(userODRepository.findByEmail("andrej5@gmail.com")).thenReturn(Optional.of(user));
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.createEvent(eventDTO, "andrej5@gmail.com"));
        assertEquals("EventType not found", exception.getMessage());

        verify(userODRepository).findByEmail("andrej5@gmail.com");
        verify(eventTypeRepository).findById(1L);
        verify(eventRepository, never()).save(any());
    }

    @Test
    void Event_creation_throws_exception_when_event_dto_field_is_null() {
        when(userODRepository.findByEmail("andrej5@gmail.com")).thenReturn(Optional.of(user));
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.of(eventType));
        eventDTO.setName(null); // Bilo koje polje kada bi bilo null (@Validate izostavljen)

        try (MockedStatic<EventMapper> eventMapperMock = mockStatic(EventMapper.class)) {
            eventMapperMock.when(() -> EventMapper.toEntity(eventDTO, user, eventType)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> eventService.createEvent(eventDTO, "andrej5@gmail.com"));
            assertEquals("Event mapping failed", exception.getMessage());

            verify(userODRepository).findByEmail("andrej5@gmail.com");
            verify(eventTypeRepository).findById(1L);
            verify(eventRepository, never()).save(any());
        }
    }


}