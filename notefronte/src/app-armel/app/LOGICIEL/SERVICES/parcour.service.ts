import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Statistiques } from '../MODELS/statistiques';

@Injectable({
  providedIn: 'root'
})
export class ParcourService {
  private apiUrl = 'http://localhost:8060/api/auth/de/all-parcours';
  private apiNivUrl = 'http://localhost:8060/api/auth/comptable';
  private apiStatsUrl = 'http://localhost:8060/api/auth/comptable/stats';
  private apiStatisUrl = 'http://localhost:8060/api/auth/comptable';
  constructor(private http: HttpClient) {}

  // Méthode pour récupérer les parcours depuis le backend
  getParcours(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getStats(): Observable<any[]> {
    return this.http.get<any[]>(this.apiStatsUrl);
  }
  // Méthode pour obtenir les statistiques des étudiants par parcours
  getStatistiquesParParcours(parcoursId: Number): Observable<Statistiques[]> {
    const url = `${this.apiStatisUrl}/statistiques-etudiants/${parcoursId}`;
    return this.http.get<Statistiques[]>(url);
  }


  // Méthode pour obtenir les niveaux par parcoursId
  getNiveauxByParcours(parcoursId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiNivUrl}/niveau/${parcoursId}`);
  }
}
