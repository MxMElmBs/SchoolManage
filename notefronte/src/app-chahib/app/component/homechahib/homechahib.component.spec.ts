import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomechahibComponent } from './homechahib.component';

describe('HomechahibComponent', () => {
  let component: HomechahibComponent;
  let fixture: ComponentFixture<HomechahibComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomechahibComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomechahibComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
