import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Professeur } from '../../models/professeur/professeur';

@Injectable({
  providedIn: 'root'
})
export class ProfesseurService {

  private apiUrl = 'http://localhost:8060/api/auth/de/all-prof'

  constructor(private http: HttpClient) { }

  getProfesseurs(): Observable<Professeur[]> {
    return this.http.get<Professeur[]>(this.apiUrl);
  }
}
