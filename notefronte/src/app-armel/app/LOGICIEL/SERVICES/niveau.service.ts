import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NiveauService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable';
  
  constructor(private http: HttpClient) { }


  getNiveaux(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/niveau`);
  }

  getNiveauxByParcours(parcoursId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/niveau/${parcoursId}`);
  }
}
