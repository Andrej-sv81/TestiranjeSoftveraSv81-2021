import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {


  loginForm!: FormGroup;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router, private fb: FormBuilder){}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  login() {
    this.authService.login(
      this.loginForm.controls['email'].getRawValue()!,
      this.loginForm.controls['password'].getRawValue()!
    ).subscribe({
      next: () => {
        this.errorMessage = '';
        this.router.navigate(['/main']);
      },
      error: (err) => {
        if (err.status === 404 && err.error === 'Bad Credentials') {
          this.errorMessage = 'Bad Credentials';
        } else if (err.status === 403 && err.error === 'account not activated') {
          this.errorMessage = 'Account not activated';
        } else {
          this.errorMessage = 'Login failed!';
        }
      }
    });
  }
}
