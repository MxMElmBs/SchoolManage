import { TestBed } from '@angular/core/testing';

import { IEtudiantServicesService } from './ietudiant-services.service';

describe('IEtudiantServicesService', () => {
  let service: IEtudiantServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IEtudiantServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
