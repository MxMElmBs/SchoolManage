import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageComptaComponent } from './manage-compta.component';

describe('ManageComptaComponent', () => {
  let component: ManageComptaComponent;
  let fixture: ComponentFixture<ManageComptaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageComptaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageComptaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
