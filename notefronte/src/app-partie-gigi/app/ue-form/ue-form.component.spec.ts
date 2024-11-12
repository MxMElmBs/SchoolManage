import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UeFormComponent } from './ue-form.component';

describe('UeFormComponent', () => {
  let component: UeFormComponent;
  let fixture: ComponentFixture<UeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UeFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
