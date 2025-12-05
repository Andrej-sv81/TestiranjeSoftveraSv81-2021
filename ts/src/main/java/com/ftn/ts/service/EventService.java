package com.ftn.ts.service;

import com.ftn.ts.dto.AgendaItemDTO;
import com.ftn.ts.dto.EventDTO;
import com.ftn.ts.dto.EventMapper;
import com.ftn.ts.model.Event;
import com.ftn.ts.model.EventAgendaItem;
import com.ftn.ts.model.EventType;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.repository.EventAgendaRepository;
import com.ftn.ts.repository.EventRepository;
import com.ftn.ts.repository.EventTypeRepository;
import com.ftn.ts.repository.UserODRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private UserODRepository userODRepository;
    @Autowired
    private EventAgendaRepository agendaRepository;

    public Event createEvent(EventDTO dto, String email) {
        UserOD user = userODRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        EventType type = eventTypeRepository.findById(dto.getEventTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("EventType not found"));
        
        Event event = EventMapper.toEntity(dto, user, type);
        if(event == null){
            throw new RuntimeException("Event mapping failed");
        }

        Event saved = eventRepository.save(event);
        return saved;
    }

    public EventAgendaItem addAgendaItem(Long eventId, AgendaItemDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException ("Event not found"));

        EventAgendaItem item = EventMapper.toAgendaEntity(dto, event);
        if(item == null){
            throw new RuntimeException("Agenda item mapping failed");
        }
        
        EventAgendaItem saved = agendaRepository.save(item);
        return saved;
    }

    public void addEventType(String name){
        EventType type = new EventType();
        type.setName(name);
        eventTypeRepository.save(type);
    }

    
}
