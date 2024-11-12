import { TestBed } from '@angular/core/testing';

import { UejeffService } from './uejeff.service';

describe('UejeffService', () => {
  let service: UejeffService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UejeffService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
