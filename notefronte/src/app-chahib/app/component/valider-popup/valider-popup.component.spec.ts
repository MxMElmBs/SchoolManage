import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValiderPopupComponent } from './valider-popup.component';

describe('ValiderPopupComponent', () => {
  let component: ValiderPopupComponent;
  let fixture: ComponentFixture<ValiderPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValiderPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValiderPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
