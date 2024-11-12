import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VolumeHoraireComponent } from './volume-horaire.component';

describe('VolumeHoraireComponent', () => {
  let component: VolumeHoraireComponent;
  let fixture: ComponentFixture<VolumeHoraireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VolumeHoraireComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VolumeHoraireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
