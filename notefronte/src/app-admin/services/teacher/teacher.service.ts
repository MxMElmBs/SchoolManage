import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../../model/Utilisateur';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private apiUrl = 'http://192.168.1.79:8060/api/user'; // Remplacez par votre URL d'API

  constructor(private http: HttpClient) {}

  creerComptesUserTeach(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create/professeur`, {});
  }

  getUserDetailsByTeach(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/professeur`);
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

  updateUserPro(user: Utilisateur): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/update/pro`, user, this.httpOptions());
  }

  private httpOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
  }


}
