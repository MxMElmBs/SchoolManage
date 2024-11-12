import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtudiantjeffComponent } from './etudiantjeff.component';

describe('EtudiantjeffComponent', () => {
  let component: EtudiantjeffComponent;
  let fixture: ComponentFixture<EtudiantjeffComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtudiantjeffComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EtudiantjeffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
