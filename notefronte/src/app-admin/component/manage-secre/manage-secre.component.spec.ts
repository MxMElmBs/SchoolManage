import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageSecreComponent } from './manage-secre.component';

describe('ManageSecreComponent', () => {
  let component: ManageSecreComponent;
  let fixture: ComponentFixture<ManageSecreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageSecreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageSecreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
