import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterPUPComponent } from './register-pup.component';

describe('RegisterPUPComponent', () => {
  let component: RegisterPUPComponent;
  let fixture: ComponentFixture<RegisterPUPComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterPUPComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterPUPComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
