import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumecahierComponent } from './resumecahier.component';

describe('ResumecahierComponent', () => {
  let component: ResumecahierComponent;
  let fixture: ComponentFixture<ResumecahierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumecahierComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResumecahierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
