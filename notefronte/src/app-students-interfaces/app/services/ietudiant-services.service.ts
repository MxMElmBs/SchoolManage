import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EtudiantCours } from '../models/EtudiantCours';
import { TypeSemestre } from '../models/TypeSemestre.enum';
import { Observable } from 'rxjs';
import { EtudiantConnect } from '../models/EtudiantConnect';
import { EtudiantNoteMaxDto } from '../models/EtudiantNoteMaxDto';

@Injectable({
  providedIn: 'root'
})
export class IEtudiantServicesService {
  private apiUrl = 'http://localhost:8060/api/auth/etudiant';
  constructor(private http: HttpClient) { }

  // Récupérer les cours par idUser

 /* getCoursByIdUser(idUser: number): Observable<EtudiantCours[]> {
    const url = `${this.apiUrl}/${idUser}/cours`;
    return this.http.get<EtudiantCours[]>(url);
  }*/

  getCoursByIdUser(idUser: number): Observable<EtudiantCours[]> {
    return this.http.get<EtudiantCours[]>(`${this.apiUrl}/${idUser}/cours`);
  }

  // Récupérer les cours par idUser et semestre
  // Method to get courses by userId and semester
  getCoursByIdUserAndSemestre(idUser: number, semestre: string): Observable<EtudiantCours[]> {
    const url = `${this.apiUrl}/${idUser}/semestre/${semestre}`;
    return this.http.get<EtudiantCours[]>(url);
  }

  getConnectedEtudiant(userId: number): Observable<EtudiantConnect> {
    return this.http.get<EtudiantConnect>(`${this.apiUrl}/connect/${userId}`);
  }

  getNotesByEtudiantId(idUser: number): Observable<EtudiantNoteMaxDto[]> {
    return this.http.get<EtudiantNoteMaxDto[]>(`${this.apiUrl}/${idUser}/notemax`);
  }

}
