import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CahiertextedehistoComponent } from './cahiertextedehisto.component';

describe('CahiertextedehistoComponent', () => {
  let component: CahiertextedehistoComponent;
  let fixture: ComponentFixture<CahiertextedehistoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CahiertextedehistoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CahiertextedehistoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
