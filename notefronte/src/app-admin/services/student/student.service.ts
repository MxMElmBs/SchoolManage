import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../../model/Utilisateur';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = 'http://192.168.1.79:8060/api/user'; // Remplacez par votre URL d'API

  constructor(private http: HttpClient) {}

  creerComptesUserStudent(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create/etudiant`, {});
  }

  getUserDetailsByStudent(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/etudiant`);
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

  countByRole(role: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`, {
      params: { role }
    });
  }

  sendCredentials(toEmail: string, username: string, password: string): Observable<string> {
    const params = new HttpParams()
      .set('toEmail', toEmail)
      .set('username', username)
      .set('password', password);

    return this.http.get<string>(`${this.apiUrl}/sendCredentials`, { params });
  }

  updateUserEtudiant(user: Utilisateur): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/update/etu`, user, this.httpOptions());
  }

  private httpOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
  }
}
