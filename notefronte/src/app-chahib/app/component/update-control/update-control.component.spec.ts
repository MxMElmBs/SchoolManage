import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateControlComponent } from './update-control.component';

describe('UpdateControlComponent', () => {
  let component: UpdateControlComponent;
  let fixture: ComponentFixture<UpdateControlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateControlComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
