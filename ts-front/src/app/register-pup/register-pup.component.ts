import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserPUP } from '../models/user-pup';
import { RegistrationService } from '../registration.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-pup',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register-pup.component.html',
  styleUrl: './register-pup.component.css'
})
export class RegisterPUPComponent {

  registerForm!: FormGroup;
  errorMessage = '';

  constructor(private service: RegistrationService, private router: Router, private fb: FormBuilder){}

  ngOnInit(): void{
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      name: ['', [Validators.required, Validators.maxLength(50)]],
      address: ['', [Validators.required, Validators.maxLength(100)]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9\\-\\+]{6,15}$')]],
      description: ['', [Validators.required, Validators.maxLength(512)]],
    });
  }

  registerOD(){
    let user: UserPUP = {
      email: this.registerForm.controls['email'].getRawValue()!,
      password: this.registerForm.controls['password'].getRawValue()!,
      name: this.registerForm.controls['name'].getRawValue()!,
      address: this.registerForm.controls['address'].getRawValue()!,
      phone: this.registerForm.controls['phone'].getRawValue()!,
      description: this.registerForm.controls['description'].getRawValue()!,
    }

    this.service.registerPUP(user).subscribe({
      next: (response: any) => {
        //TODO: navigate to picture upload!
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        this.errorMessage = err.error || 'Registration failed!';
      }
    });
  }

  click() {}
}
