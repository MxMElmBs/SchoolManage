import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsIDashBComponent } from './students-idash-b.component';

describe('StudentsIDashBComponent', () => {
  let component: StudentsIDashBComponent;
  let fixture: ComponentFixture<StudentsIDashBComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsIDashBComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsIDashBComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
