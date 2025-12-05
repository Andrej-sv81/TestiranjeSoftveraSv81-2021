import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventService } from '../event.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AgendaItemDTO } from '../models/agendaItemDTO';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './agenda.component.html',
  styleUrl: './agenda.component.css'
})
export class AgendaComponent {
  eventId!: number;
  agendaForm!: FormGroup;
  agendaList: AgendaItemDTO[] = [];

  constructor(private route: ActivatedRoute, private eventService: EventService, private fb: FormBuilder) {}

  ngOnInit() {
    this.eventId = +this.route.snapshot.queryParamMap.get('eventId')!;
    this.agendaForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(1000)]],
      startTime: ['', [Validators.required, Validators.pattern('^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$')]],
      endTime: ['', [Validators.required, Validators.pattern('^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$')]],
      location: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  addAgendaItem() {
    if (this.agendaForm.valid) {
      const item: AgendaItemDTO = this.agendaForm.value;
      this.agendaList.push(item);
        this.eventService.addAgendaItem(this.eventId, item).subscribe(() => {
          console.log('Agenda item added successfully');
      });
      this.agendaForm.reset();
    }
  }
}
