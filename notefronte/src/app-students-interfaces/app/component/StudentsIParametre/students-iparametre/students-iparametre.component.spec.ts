import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentsIParametreComponent } from './students-iparametre.component';

describe('StudentsIParametreComponent', () => {
  let component: StudentsIParametreComponent;
  let fixture: ComponentFixture<StudentsIParametreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentsIParametreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsIParametreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
