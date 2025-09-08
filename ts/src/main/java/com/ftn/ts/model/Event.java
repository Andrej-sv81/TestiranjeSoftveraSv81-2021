package com.ftn.ts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserOD organizer;

    @ManyToOne
    private EventType eventType;

    private String name;
    private String description;
    private int maxParticipants;

    @Enumerated(EnumType.STRING)
    private PrivacyType privacyType;

    private String location;
    private LocalDateTime eventDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAgendaItem> agenda = new ArrayList<>();
}
