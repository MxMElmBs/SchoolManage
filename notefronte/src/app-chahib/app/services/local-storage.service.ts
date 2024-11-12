import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }

  getStudentId(): number {
    return Number(localStorage.getItem('etudiantId')) || 0;
  }

  setStudentId(id: number): void {
    localStorage.setItem('etudiantId', id.toString());
  }
}
