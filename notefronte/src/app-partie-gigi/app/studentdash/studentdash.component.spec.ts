import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentdashComponent } from './studentdash.component';

describe('StudentdashComponent', () => {
  let component: StudentdashComponent;
  let fixture: ComponentFixture<StudentdashComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentdashComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentdashComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
