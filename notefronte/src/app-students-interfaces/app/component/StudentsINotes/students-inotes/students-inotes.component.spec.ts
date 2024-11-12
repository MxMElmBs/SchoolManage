import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsINotesComponent } from './students-inotes.component';

describe('StudentsINotesComponent', () => {
  let component: StudentsINotesComponent;
  let fixture: ComponentFixture<StudentsINotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsINotesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsINotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
