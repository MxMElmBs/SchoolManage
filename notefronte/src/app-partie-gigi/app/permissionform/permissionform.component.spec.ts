import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionformComponent } from './permissionform.component';

describe('PermissionformComponent', () => {
  let component: PermissionformComponent;
  let fixture: ComponentFixture<PermissionformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermissionformComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PermissionformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
