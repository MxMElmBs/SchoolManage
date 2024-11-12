import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardjeffComponent } from './dashboardjeff.component';

describe('DashboardComponent', () => {
  let component: DashboardjeffComponent;
  let fixture: ComponentFixture<DashboardjeffComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardjeffComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardjeffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
