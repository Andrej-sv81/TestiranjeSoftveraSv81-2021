import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventDTO } from './models/eventDTO';
import { AgendaItemDTO } from './models/agendaItemDTO';


@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = 'http://localhost:8080/api/events';

  constructor(private http: HttpClient) {}

  createEvent(event: EventDTO): Observable<number> {
    return this.http.post<number>(`${this.baseUrl}`, event);
  }

  addAgendaItem(eventId: number, item: AgendaItemDTO): Observable<number> {
    return this.http.post<number>(`${this.baseUrl}/${eventId}/agenda`, item);
  }

  // getAgenda(eventId: number): Observable<AgendaItemDTO[]> {
  //   return this.http.get<AgendaItemDTO[]>(`${this.baseUrl}/${eventId}/agenda`);
  // }
}
