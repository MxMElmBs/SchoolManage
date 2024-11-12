import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DirecEtude } from '../../model/DirecEtude';
import { Utilisateur } from '../../model/Utilisateur';
@Injectable({
  providedIn: 'root'
})
export class DeService {

  private apiUrl = 'http://localhost:8060/api/user'; // Remplacez par votre URL d'API
  private apiUrl2 = 'http://localhost:8060/api/de'; // Remplacez par votre URL d'API

  constructor(private http: HttpClient) {}

  

  creerComptesUserDE(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create/directeur`, {});
  }

  getUserDetailsByDE(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/direc-etude`);
  }

  countByRole(role: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`, {
      params: { role }
    });
  }

  updateUser(idUser: number, username: string, password: string, role: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${idUser}`, {}, {
      params: {
        username,
        password,
        role
      }
    });
  }

  createDirecEtude(direcEtude: DirecEtude): Observable<DirecEtude> {
    return this.http.post<DirecEtude>(this.apiUrl2, direcEtude);
  }

  deleteDirecEtude(idDe: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl2}/${idDe}`);
  }

  sendCredentials(toEmail: string, username: string, password: string): Observable<string> {
    const params = new HttpParams()
      .set('toEmail', toEmail)
      .set('username', username)
      .set('password', password);

    return this.http.get<string>(`${this.apiUrl}/sendCredentials`, { params });
  }

  updateUserDE(user: Utilisateur): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/update/de`, user, this.httpOptions());
  }

  private httpOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
  }

}
