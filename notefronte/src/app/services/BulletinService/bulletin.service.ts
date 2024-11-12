import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NiveauStats } from '../../models/NiveauStats/niveau-stats';

@Injectable({
  providedIn: 'root'
})
export class BulletinService {

  private baseUrl = 'http://localhost:8060/api/auth/de';
  private apiUrl = 'http://localhost:8060/api/auth/de/niveaux/stats';

  constructor(private http: HttpClient) { }

  // Méthode pour récupérer les détails des UE par semestre
  getUeDetailsParSemestre(etudiantId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/etudiants/${etudiantId}/ue-details-par-semestre`);
  }

  // Générer le bulletin PDF
  genererBulletinPDF(id: number): Observable<Blob> {
    const url = `${this.baseUrl}/etudiants/${id}/bulletin-pdf`;
    return this.http.get(url, { responseType: 'blob' });
  }

  // Générer le bulletin Excel
  genererBulletinExcel(id: number): Observable<Blob> {
    const url = `${this.baseUrl}/etudiants/${id}/bulletin-excel`;
    return this.http.get(url, { responseType: 'blob' });
  }

  // Méthode pour envoyer le bulletin PDF par e-mail
  envoyerBulletinParEmail(id: number): Observable<any> {
    const url = `${this.baseUrl}/bulletin/envoyer/${id}`;
    return this.http.post(url, null, { responseType: 'text' });  // Utilisation de responseType 'text' pour obtenir un message texte en réponse
  }

  getStatsParNiveau(): Observable<NiveauStats[]> {
    return this.http.get<NiveauStats[]>(this.apiUrl);
  }
}
