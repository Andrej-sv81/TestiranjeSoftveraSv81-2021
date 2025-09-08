package com.ftn.ts.repository;

import com.ftn.ts.model.EventAgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAgendaRepository extends JpaRepository<EventAgendaItem, Long>{
}
