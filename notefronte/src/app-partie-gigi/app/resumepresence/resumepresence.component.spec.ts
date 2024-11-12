import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumepresenceComponent } from './resumepresence.component';

describe('ResumepresenceComponent', () => {
  let component: ResumepresenceComponent;
  let fixture: ComponentFixture<ResumepresenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumepresenceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResumepresenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
