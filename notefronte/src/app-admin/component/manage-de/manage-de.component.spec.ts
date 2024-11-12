import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageDeComponent } from './manage-de.component';

describe('ManageDeComponent', () => {
  let component: ManageDeComponent;
  let fixture: ComponentFixture<ManageDeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageDeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageDeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
