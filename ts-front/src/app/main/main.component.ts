import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EventService } from '../event.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {
  eventForm!: FormGroup;

  constructor(private fb: FormBuilder, private eventService: EventService, private router: Router) {}

  ngOnInit(): void {
    this.eventForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(150)]],
      description: ['', [Validators.required, Validators.maxLength(2000)]],
      maxParticipants: [1, [Validators.required, Validators.min(1), Validators.max(10000)]],
      privacyType: ['', [Validators.required, Validators.pattern('^(OPEN|CLOSED)$')]],
      location: ['', [Validators.required, Validators.maxLength(255)]],
      eventDate: ['', [Validators.required, Validators.pattern('^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$')]],
      eventTypeId: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.eventForm.valid) {
    this.eventService.createEvent(this.eventForm.value).subscribe({
      next: (response: any) => {
        this.router.navigate(['/agenda'], { queryParams: { eventId: response } });
      }
    });
    }
  }
}
