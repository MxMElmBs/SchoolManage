import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermpopupComponent } from './permpopup.component';

describe('PermpopupComponent', () => {
  let component: PermpopupComponent;
  let fixture: ComponentFixture<PermpopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermpopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PermpopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
