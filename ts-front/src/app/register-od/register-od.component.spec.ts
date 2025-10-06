import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterODComponent } from './register-od.component';
import { RegistrationService } from '../registration.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('RegisterODComponent', () => {
  let component: RegisterODComponent;
  let fixture: ComponentFixture<RegisterODComponent>;
  let registrationService: jasmine.SpyObj<RegistrationService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const registrationServiceSpy = jasmine.createSpyObj('RegistrationService', ['registerOD']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [RegisterODComponent, ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: RegistrationService, useValue: registrationServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterODComponent);
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
        surname: '',
        address: '',
        phone: ''
      });
    });
  });

  //valicacija forme
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
      passwordControl.setValue('123456');
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

    it('should require surname', () => {
      const surnameControl = component.registerForm.controls['surname'];
      surnameControl.setValue('');
      expect(surnameControl.hasError('required')).toBeTrue();
    });

    it('should invalidate surname longer than 50 characters', () => {
      const surnameControl = component.registerForm.controls['surname'];
      surnameControl.setValue('a'.repeat(51));
      expect(surnameControl.hasError('maxlength')).toBeTrue();
    });

    it('should accept valid surname', () => {
      const surnameControl = component.registerForm.controls['surname'];
      surnameControl.setValue('Mitrovic');
      expect(surnameControl.valid).toBeTrue();
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
      phoneControl.setValue('asdfghj');
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
        surname: 'Mitrovic',
        address: 'Vuka Karadzica 10',
        phone: '+38166344335'
      });
      expect(component.registerForm.valid).toBeTrue();
    });
  });

  //-------------------------------------------------------------------------------------------------
  //-------------------------------------------------------------------------------------------------
  //validacija metode registerOD
  describe('Method registerOD', () => {
    beforeEach(() => {
      component.registerForm.setValue({
        email: 'andrej5@gmail.com',
        password: '123asd123',
        name: 'Andrej',
        surname: 'Mitrovic',
        address: 'Vuka Karadzica 10',
        phone: '+38166344335'
      });
    });


    it('should call service registerOD and navigate on successful registration', fakeAsync(() => {
      const user = { ...component.registerForm.value };
      registrationService.registerOD.and.returnValue(of({}));
  
      component.registerOD();
      tick();

      expect(registrationService.registerOD).toHaveBeenCalledWith(user);
      expect(router.navigate).toHaveBeenCalledWith(['/picture-od'], { queryParams: { email: user.email } });
    }));

    it('should set errorMessage on failed registration', fakeAsync(() => {
      const errorResponse = { error: 'Email already exists' };
      registrationService.registerOD.and.returnValue(throwError(() => errorResponse));

      component.registerOD();
      tick();

      expect(component.errorMessage).toBe('Email already exists');
      expect(router.navigate).not.toHaveBeenCalled();
    }));

    it('should set a default error message if error response has no body', fakeAsync(() => {
      const errorResponse = {};
      registrationService.registerOD.and.returnValue(throwError(() => errorResponse));

      component.registerOD();
      tick();

      expect(component.errorMessage).toBe('Registration failed!');
    }));

  });
});
