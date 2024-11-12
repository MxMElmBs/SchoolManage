import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Bts1Component } from './bts1.component';

describe('Bts1Component', () => {
  let component: Bts1Component;
  let fixture: ComponentFixture<Bts1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Bts1Component]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(Bts1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
