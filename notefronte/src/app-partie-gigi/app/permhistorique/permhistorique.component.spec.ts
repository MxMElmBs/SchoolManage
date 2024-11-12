import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermhistoriqueComponent } from './permhistorique.component';

describe('PermhistoriqueComponent', () => {
  let component: PermhistoriqueComponent;
  let fixture: ComponentFixture<PermhistoriqueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermhistoriqueComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PermhistoriqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
