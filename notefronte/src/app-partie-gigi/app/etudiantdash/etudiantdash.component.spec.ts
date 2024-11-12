import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtudiantdashComponent } from './etudiantdash.component';

describe('EtudiantdashComponent', () => {
  let component: EtudiantdashComponent;
  let fixture: ComponentFixture<EtudiantdashComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtudiantdashComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EtudiantdashComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
