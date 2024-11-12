import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailpermComponent } from './detailperm.component';

describe('DetailpermComponent', () => {
  let component: DetailpermComponent;
  let fixture: ComponentFixture<DetailpermComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailpermComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailpermComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
