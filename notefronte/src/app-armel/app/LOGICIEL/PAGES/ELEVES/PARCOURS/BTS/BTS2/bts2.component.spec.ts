import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Bts2Component } from './bts2.component';

describe('Bts2Component', () => {
  let component: Bts2Component;
  let fixture: ComponentFixture<Bts2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Bts2Component]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(Bts2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
