import { TestBed } from '@angular/core/testing';

import { EtudiantjeffService } from './etudiantjeff.service';

describe('EtudiantjeffService', () => {
  let service: EtudiantjeffService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EtudiantjeffService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
