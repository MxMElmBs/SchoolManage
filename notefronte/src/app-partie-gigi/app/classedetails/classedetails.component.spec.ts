import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassedetailsComponent } from './classedetails.component';

describe('ClassedetailsComponent', () => {
  let component: ClassedetailsComponent;
  let fixture: ComponentFixture<ClassedetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassedetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClassedetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
