import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocProfComponent } from './doc-prof.component';

describe('DocProfComponent', () => {
  let component: DocProfComponent;
  let fixture: ComponentFixture<DocProfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocProfComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocProfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
