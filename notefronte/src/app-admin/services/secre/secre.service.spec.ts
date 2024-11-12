/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { OtherUserService } from './otheruser.service';

describe('Service: OtherUser', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OtherUserService]
    });
  });

  it('should ...', inject([OtherUserService], (service: OtherUserService) => {
    expect(service).toBeTruthy();
  }));
});
