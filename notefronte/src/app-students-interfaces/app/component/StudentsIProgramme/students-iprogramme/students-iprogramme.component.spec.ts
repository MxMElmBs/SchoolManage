import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsIProgrammeComponent } from './students-iprogramme.component';

describe('StudentsIProgrammeComponent', () => {
  let component: StudentsIProgrammeComponent;
  let fixture: ComponentFixture<StudentsIProgrammeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsIProgrammeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsIProgrammeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
