import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OtherUser } from '../model/OtherUser';
import { DirecEtude } from '../model/DirecEtude';
import { Utilisateur } from '../model/Utilisateur';
import { Parcours } from '../model/Parcours';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8060/api/auth/admin';

  constructor(private http: HttpClient) { }

  // Appel POST pour ajouter un directeur d'étude
  ajouterOtherUser(dto: OtherUser): Observable<OtherUser> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<OtherUser>(`${this.apiUrl}/ajouter-co`, dto, { headers });
  }

  // Appel GET pour créer des utilisateurs pour d'autres utilisateurs sans utilisateur
  creerOtherUtilisateurs(): Observable<string> {
    return this.http.get(`${this.apiUrl}/creer-utilisateurs-co`, { responseType: 'text' });
  }

  creerUtilisateursPourEtudiants(): Observable<string> {
    return this.http.get(`${this.apiUrl}/creer-utilisateurs-student`, { responseType: 'text' });
  }

  creerUtilisateursProf(): Observable<string> {
    return this.http.get(`${this.apiUrl}/creer-utilisateurs-prof`, { responseType: 'text' });
  }

  // Appel POST pour ajouter un directeur d'étude
  ajouterDEUser(dto: DirecEtude): Observable<DirecEtude> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<DirecEtude>(`${this.apiUrl}/ajouter-de`, dto, { headers });
  }

  // Appel GET pour créer des utilisateurs pour d'autres utilisateurs sans utilisateur
  creerDEUtilisateurs(): Observable<string> {
    return this.http.get(`${this.apiUrl}/creer-utilisateurs-de`, { responseType: 'text' });
  }

  sendCredentials(user: Utilisateur): Observable<any> {
    const params = new HttpParams()
        .set('email', user.email)
        .set('username', user.username)
        .set('password', user.password);

    return this.http.get<any>(`${this.apiUrl}/sendCredentials`, { params });
}


  getUserByRole(role: string): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/getUserByRole`, {
      params: {
        role: role
      }
    });
  }

  updateUserPrint(userDto: Utilisateur): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/updateUserPrint`, userDto);
  }

  countByRole(role: string): Observable<number> {
    const params = new HttpParams().set('role', role);  // Ajoute le rôle en tant que paramètre de requête
    return this.http.get<number>(`${this.apiUrl}/count`, { params });
  }

  getParcours(): Observable<Parcours[]> {
    return this.http.get<Parcours[]>(`${this.apiUrl}/all-parcours`);  // Récupère tous les parcours
  }

}
