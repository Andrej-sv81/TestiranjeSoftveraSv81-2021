import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PicturePUPComponent } from './picture-pup.component';

describe('PicturePUPComponent', () => {
  let component: PicturePUPComponent;
  let fixture: ComponentFixture<PicturePUPComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PicturePUPComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PicturePUPComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
