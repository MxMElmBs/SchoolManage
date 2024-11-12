import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PoppresenceComponent } from './poppresence.component';

describe('PoppresenceComponent', () => {
  let component: PoppresenceComponent;
  let fixture: ComponentFixture<PoppresenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PoppresenceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PoppresenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
