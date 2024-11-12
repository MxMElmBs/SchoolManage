import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefitechComponent } from './defitech.component';

describe('DefitechComponent', () => {
  let component: DefitechComponent;
  let fixture: ComponentFixture<DefitechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DefitechComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DefitechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
