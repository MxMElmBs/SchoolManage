import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsIAcceuilComponent } from './students-iacceuil.component';

describe('StudentsIAcceuilComponent', () => {
  let component: StudentsIAcceuilComponent;
  let fixture: ComponentFixture<StudentsIAcceuilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsIAcceuilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsIAcceuilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
