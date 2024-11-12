import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudenttabComponent } from './studenttab.component';

describe('StudenttabComponent', () => {
  let component: StudenttabComponent;
  let fixture: ComponentFixture<StudenttabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudenttabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudenttabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
