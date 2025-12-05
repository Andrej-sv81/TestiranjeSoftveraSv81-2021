import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterPUPComponent } from './register-pup.component';
import { RegistrationService } from '../registration.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('RegisterPUPComponent', () => {
  let component: RegisterPUPComponent;
  let fixture: ComponentFixture<RegisterPUPComponent>;
  let registrationService: jasmine.SpyObj<RegistrationService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const registrationServiceSpy = jasmine.createSpyObj('RegistrationService', ['registerPUP']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [RegisterPUPComponent, ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: RegistrationService, useValue: registrationServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterPUPComponent);
    component = fixture.componentInstance;
    registrationService = TestBed.inject(RegistrationService) as jasmine.SpyObj<RegistrationService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture.detectChanges();
  });


  //-------------------------------------------------------------------------------------------------
  //-------------------------------------------------------------------------------------------------
  describe('Component and Form setup', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should initialize the form with empty fields', () => {
      expect(component.registerForm.value).toEqual({
        email: '',
        password: '',
        name: '',
        description: '',
        address: '',
        phone: ''
      });
    });
  });

  describe('Form Validation', () => {

    it('should make email field required', () => {
      const emailControl = component.registerForm.controls['email'];
      emailControl.setValue('');
      expect(emailControl.hasError('required')).toBeTrue();
    });

    it('should invalidate incorrect email format', () => {
      const emailControl = component.registerForm.controls['email'];
      emailControl.setValue('andrej5');
      expect(emailControl.hasError('email')).toBeTrue();
    });

    it('should validate correct email format', () => {
      const emailControl = component.registerForm.controls['email'];
      emailControl.setValue('andrej5@gmail.com');
      expect(emailControl.valid).toBeTrue();
    });

    it('should require password', () => {
      const passwordControl = component.registerForm.controls['password'];
      passwordControl.setValue('');
      expect(passwordControl.hasError('required')).toBeTrue();
    });

    it('should enforce password minlength of 6', () => {
      const passwordControl = component.registerForm.controls['password'];
      passwordControl.setValue('12345');
      expect(passwordControl.hasError('minlength')).toBeTrue();
    });

    it('should accept password with length 6 or more', () => {
      const passwordControl = component.registerForm.controls['password'];
      passwordControl.setValue('123asd123');
      expect(passwordControl.valid).toBeTrue();
    });

    it('should require name', () => {
      const nameControl = component.registerForm.controls['name'];
      nameControl.setValue('');
      expect(nameControl.hasError('required')).toBeTrue();
    });

    it('should invalidate name longer than 50 characters', () => {
      const nameControl = component.registerForm.controls['name'];
      nameControl.setValue('a'.repeat(51));
      expect(nameControl.hasError('maxlength')).toBeTrue();
    });

    it('should accept valid name', () => {
      const nameControl = component.registerForm.controls['name'];
      nameControl.setValue('Andrej');
      expect(nameControl.valid).toBeTrue();
    });

    it('should make description field required', () => {
      const descriptionControl = component.registerForm.controls['description'];
      descriptionControl.setValue('');
      expect(descriptionControl.hasError('required')).toBeTrue();
    });

    it('should invalidate description longer than 255 characters', () => {
      const descriptionControl = component.registerForm.controls['description'];
      descriptionControl.setValue('a'.repeat(256));
      expect(descriptionControl.hasError('maxlength')).toBeTrue();
    });

    it('should accept valid description', () => {
      const descriptionControl = component.registerForm.controls['description'];
      descriptionControl.setValue('Mitrovic');
      expect(descriptionControl.valid).toBeTrue();
    });

    it('should require address', () => {
      const addressControl = component.registerForm.controls['address'];
      addressControl.setValue('');
      expect(addressControl.hasError('required')).toBeTrue();
    });

    it('should invalidate address longer than 100 characters', () => {
      const addressControl = component.registerForm.controls['address'];
      addressControl.setValue('a'.repeat(101));
      expect(addressControl.hasError('maxlength')).toBeTrue();
    });

    it('should accept valid address', () => {
      const addressControl = component.registerForm.controls['address'];
      addressControl.setValue('Vuka Karadzica 10');
      expect(addressControl.valid).toBeTrue();
    });

    it('should require phone', () => {
      const phoneControl = component.registerForm.controls['phone'];
      phoneControl.setValue('');
      expect(phoneControl.hasError('required')).toBeTrue();
    });

    it('should invalidate phone if pattern does not match (non-numeric)', () => {
      const phoneControl = component.registerForm.controls['phone'];
      phoneControl.setValue('abc');
      expect(phoneControl.hasError('pattern')).toBeTrue();
    });

    it('should invalidate phone if pattern does not match (too short)', () => {
      const phoneControl = component.registerForm.controls['phone'];
      phoneControl.setValue('12345');
      expect(phoneControl.hasError('pattern')).toBeTrue();
    });

    it('should accept phone that matches pattern (digits)', () => {
      const phoneControl = component.registerForm.controls['phone'];
      phoneControl.setValue('123456');
      expect(phoneControl.valid).toBeTrue();
    });

    it('should accept phone that matches pattern (with + and dashes)', () => {
      const phoneControl = component.registerForm.controls['phone'];
      phoneControl.setValue('+38166344335');
      expect(phoneControl.valid).toBeTrue();
    });

    it('should be invalid when form is empty', () => {
      expect(component.registerForm.valid).toBeFalse();
    });

    it('should be valid when form is filled with valid data', () => {
      component.registerForm.setValue({
        email: 'andrej5@gmail.com',
        password: '123asd123',
        name: 'Andrej',
        description: 'Mitrovic',
        address: 'Vuka Karadzica 10',
        phone: '+38166344335'
      });
      expect(component.registerForm.valid).toBeTrue();
    });
  });


  describe('registerPUP method', () => { 
    beforeEach(() => {
      component.registerForm.setValue({
        email: 'andrej5@gmail.com',
        password: '123asd123',
        name: 'Andrej',
        description: 'Mitrovic',
        address: 'Vuka Karadzica 10',
        phone: '+38166344335'
      });
    });

    it('should call service registerPUP and navigate on successful registration', fakeAsync(() => {
      const mockUser = { ...component.registerForm.value };
      registrationService.registerPUP.and.returnValue(of({}));
      
      component.registerPUP(); 
      tick();

      expect(registrationService.registerPUP).toHaveBeenCalledWith(mockUser);
      expect(router.navigate).toHaveBeenCalledWith(['/picture-pup'], { queryParams: { email: mockUser.email } });
    }));

    it('should set errorMessage on failed registration', fakeAsync(() => {
      const errorResponse = { error: 'Email already exists' };
      registrationService.registerPUP.and.returnValue(throwError(() => errorResponse));

      component.registerPUP(); 
      tick();

      expect(component.errorMessage).toBe('Email already exists');
      expect(router.navigate).not.toHaveBeenCalled();
    }));

    it('should set a default error message if error response has no body', fakeAsync(() => {
      const errorResponse = {};
      registrationService.registerPUP.and.returnValue(throwError(() => errorResponse));

      component.registerPUP(); 
      tick();

      expect(component.errorMessage).toBe('Registration failed!');
    }));

  });
});
