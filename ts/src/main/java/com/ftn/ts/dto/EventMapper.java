package com.ftn.ts.dto;

import com.ftn.ts.model.*;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventDTO toDTO(Event e) {
        if (e == null) return null;

        EventDTO dto = new EventDTO();
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setMaxParticipants(e.getMaxParticipants());
        dto.setPrivacyType(e.getPrivacyType().name());
        dto.setLocation(e.getLocation());
        dto.setEventDate(String.valueOf(e.getEventDate()));
        dto.setEventTypeId(e.getEventType() != null ? e.getEventType().getId() : null);
        return dto;
    }

    public static Event toEntity(EventDTO dto, UserOD organizer, EventType eventType) {
        if (dto == null) return null;

        Event e = new Event();
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setMaxParticipants(dto.getMaxParticipants());
        e.setPrivacyType(PrivacyType.valueOf(dto.getPrivacyType()));
        e.setLocation(dto.getLocation());
        e.setEventDate(LocalDateTime.parse(dto.getEventDate()));
        e.setOrganizer(organizer);
        e.setEventType(eventType);
        return e;
    }

    // ---- Agenda mapiranje ----
    public static AgendaItemDTO toAgendaDTO(EventAgendaItem item) {
        if (item == null) return null;

        AgendaItemDTO dto = new AgendaItemDTO();
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setStartTime(String.valueOf(item.getStartTime()));
        dto.setEndTime(String.valueOf(item.getEndTime()));
        dto.setLocation(item.getLocation());
        return dto;
    }

    public static EventAgendaItem toAgendaEntity(AgendaItemDTO dto, Event event) {
        if (dto == null) return null;

        EventAgendaItem item = new EventAgendaItem();
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setStartTime(LocalDateTime.parse(dto.getStartTime()));
        item.setEndTime(LocalDateTime.parse(dto.getEndTime()));
        item.setLocation(dto.getLocation());
        item.setEvent(event);
        return item;
    }
}

