import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LicenceSoirComponent } from './licence-soir.component';

describe('LicenceSoirComponent', () => {
  let component: LicenceSoirComponent;
  let fixture: ComponentFixture<LicenceSoirComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenceSoirComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LicenceSoirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
