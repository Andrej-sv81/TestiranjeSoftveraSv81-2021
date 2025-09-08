package com.ftn.ts.controller;

import com.ftn.ts.dto.AgendaItemDTO;
import com.ftn.ts.dto.EventDTO;
import com.ftn.ts.model.Event;
import com.ftn.ts.model.EventAgendaItem;
import com.ftn.ts.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
//    @PreAuthorize("ROLE_USER_OD")
    public ResponseEntity<Long> createEvent(@RequestBody @Valid EventDTO dto, Principal principal) {
        Event created = eventService.createEvent(dto, principal.getName());
        return ResponseEntity.ok(created.getId());
    }

    @PostMapping("/{eventId}/agenda")
    public ResponseEntity<Long> addAgendaItem(@PathVariable Long eventId, @RequestBody @Valid AgendaItemDTO dto){
        EventAgendaItem created = eventService.addAgendaItem(eventId, dto);
        return ResponseEntity.ok(created.getId());
    }
}
