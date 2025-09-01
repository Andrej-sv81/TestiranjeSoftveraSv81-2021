import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PictureODComponent } from './picture-od.component';

describe('PictureODComponent', () => {
  let component: PictureODComponent;
  let fixture: ComponentFixture<PictureODComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PictureODComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PictureODComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
