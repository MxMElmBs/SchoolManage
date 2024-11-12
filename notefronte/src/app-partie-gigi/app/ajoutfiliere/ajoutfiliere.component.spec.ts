import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutfiliereComponent } from './ajoutfiliere.component';

describe('AjoutfiliereComponent', () => {
  let component: AjoutfiliereComponent;
  let fixture: ComponentFixture<AjoutfiliereComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutfiliereComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutfiliereComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
