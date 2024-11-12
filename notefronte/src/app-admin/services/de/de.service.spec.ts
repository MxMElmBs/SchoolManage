/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { DeService } from './de.service';

describe('Service: De', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DeService]
    });
  });

  it('should ...', inject([DeService], (service: DeService) => {
    expect(service).toBeTruthy();
  }));
});
