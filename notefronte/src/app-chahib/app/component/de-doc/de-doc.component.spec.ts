import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeDocComponent } from './de-doc.component';

describe('DeDocComponent', () => {
  let component: DeDocComponent;
  let fixture: ComponentFixture<DeDocComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeDocComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeDocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
