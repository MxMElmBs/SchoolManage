import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlmoreComponent } from './controlmore.component';

describe('ControlmoreComponent', () => {
  let component: ControlmoreComponent;
  let fixture: ComponentFixture<ControlmoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ControlmoreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ControlmoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
