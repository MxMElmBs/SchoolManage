import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsIHeaderComponent } from './students-iheader.component';

describe('StudentsIHeaderComponent', () => {
  let component: StudentsIHeaderComponent;
  let fixture: ComponentFixture<StudentsIHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsIHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsIHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
