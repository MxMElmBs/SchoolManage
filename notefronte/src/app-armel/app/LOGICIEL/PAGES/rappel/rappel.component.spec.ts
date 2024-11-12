import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RappelComponent } from './rappel.component';

describe('RappelComponent', () => {
  let component: RappelComponent;
  let fixture: ComponentFixture<RappelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RappelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RappelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
