import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EnumsService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable';

constructor(private http: HttpClient) { }


getMentions(): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/mentions`);
}

getModalites(): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/modalites`);
}

getStatutBoursier(): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/boursier`);
}

getNiveaux(): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/niveau`);
}
}
