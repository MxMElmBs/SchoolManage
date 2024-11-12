import { TestBed } from '@angular/core/testing';

import { DocumentselectService } from './documentselect.service';

describe('DocumentselectService', () => {
  let service: DocumentselectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentselectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
