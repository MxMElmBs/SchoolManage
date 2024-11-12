import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeclassementComponent } from './declassement.component';

describe('DeclassementComponent', () => {
  let component: DeclassementComponent;
  let fixture: ComponentFixture<DeclassementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeclassementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeclassementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
