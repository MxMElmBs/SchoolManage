import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LicenceJourComponent } from './licence-jour.component';

describe('LicenceJourComponent', () => {
  let component: LicenceJourComponent;
  let fixture: ComponentFixture<LicenceJourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenceJourComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LicenceJourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
