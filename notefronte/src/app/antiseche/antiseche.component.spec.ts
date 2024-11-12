import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntisecheComponent } from './antiseche.component';

describe('AntisecheComponent', () => {
  let component: AntisecheComponent;
  let fixture: ComponentFixture<AntisecheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AntisecheComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AntisecheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
