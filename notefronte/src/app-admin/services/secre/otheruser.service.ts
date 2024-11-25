import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Utilisateur } from '../../model/Utilisateur'; // Assurez-vous que le chemin est correct
import { OtherUser } from '../../model/OtherUser'; // Assurez-vous que le chemin est correct

@Injectable({
  providedIn: 'root'
})
export class OtherUserService {

  private apiUrl = 'http://192.168.1.79:8060/api/user'; // Remplacez par votre URL d'API
  private apiUrl2 = 'http://192.168.1.79:8060/api/ouser'; // URL de votre API Spring Boot


  constructor(private http: HttpClient) {}

  creerComptesUserSecretaire(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create/secretaire`, {});
  }

  creerComptesUserComptable(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create/comptable`, {});
  }

  getUserDetailsBySecretaire(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/secretaire`);
  }

  getUserDetailsByComptable(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/comptable`);
  }
  // Méthodes pour mettre à jour et supprimer des utilisateurs
  updateUser(idUser: number, username: string, password: string, role: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${idUser}`, {}, {
      params: {
        username,
        password,
        role
      }
    });
  }


  // Méthode pour compter les utilisateurs par rôle
  countByRole(role: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`, {
      params: { role }
    });
  }

  createOtherUser(user: OtherUser): Observable<OtherUser> {
    return this.http.post<OtherUser>(this.apiUrl2, user);
  }

  updateOtherUser(id: number, user: OtherUser): Observable<void> {
    return this.http.put<void>(`${this.apiUrl2}/${id}`, user);
  }

  deleteOtherUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl2}/${id}`);
  }

  sendCredentials(toEmail: string, username: string, password: string): Observable<string> {
    const params = new HttpParams()
      .set('toEmail', toEmail)
      .set('username', username)
      .set('password', password);

    return this.http.get<string>(`${this.apiUrl}/sendCredentials`, { params });
  }

  updateUserOU(user: Utilisateur): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/update/ou`, user, this.httpOptions());
  }

  private httpOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
  }

}
