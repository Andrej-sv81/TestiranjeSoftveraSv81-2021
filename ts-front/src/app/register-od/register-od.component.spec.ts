import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterODComponent } from './register-od.component';

describe('RegisterODComponent', () => {
  let component: RegisterODComponent;
  let fixture: ComponentFixture<RegisterODComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterODComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterODComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
