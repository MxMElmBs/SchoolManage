import { TestBed } from '@angular/core/testing';

import { TableaudebordService } from './tableaudebord.service';

describe('TableaudebordService', () => {
  let service: TableaudebordService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TableaudebordService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
