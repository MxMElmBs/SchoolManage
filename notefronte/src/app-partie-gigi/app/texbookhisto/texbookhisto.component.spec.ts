import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TexbookhistoComponent } from './texbookhisto.component';

describe('TexbookhistoComponent', () => {
  let component: TexbookhistoComponent;
  let fixture: ComponentFixture<TexbookhistoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TexbookhistoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TexbookhistoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
