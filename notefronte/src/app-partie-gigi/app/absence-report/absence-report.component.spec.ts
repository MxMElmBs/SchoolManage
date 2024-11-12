import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsenceReportComponent } from './absence-report.component';

describe('AbsenceReportComponent', () => {
  let component: AbsenceReportComponent;
  let fixture: ComponentFixture<AbsenceReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbsenceReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbsenceReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
