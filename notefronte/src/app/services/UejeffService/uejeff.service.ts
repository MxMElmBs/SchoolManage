import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Uejeff } from '../../models/uejeff/uejeff';
import { TypeSemestre } from '../../models/enums/type-semestre';
import { TypeNote } from '../../models/enums/type-note';
import { EtudiantMoyenneDto } from '../../models/EtudiantMoyenneDto/etudiant-moyenne-dto';

@Injectable({
  providedIn: 'root'
})
export class UejeffService {

  private apiUrl = 'http://localhost:8060/api/auth/de/noteue';
  private baseUrl = 'http://localhost:8060/api/auth/de';

  constructor(private http:HttpClient) { }

  // Récupérer toutes les UE
  getUes(): Observable<Uejeff[]> {
    return this.http.get<Uejeff[]>(this.apiUrl);
  }

  // Récupérer une UE par ID
  getUe(id: number, selectedUE: Uejeff): Observable<Uejeff> {
    return this.http.get<Uejeff>(`${this.apiUrl}/${id}`);
  }

  // Créer une nouvelle UE
  createUe(ue: Uejeff): Observable<Uejeff> {
    return this.http.post<Uejeff>(`${this.apiUrl}`, ue);
  }

  // Mettre à jour une UE
  updateUe(id: number, ue: Uejeff): Observable<Uejeff> {
    return this.http.put<Uejeff>(`${this.apiUrl}/${id}`, ue);
  }

  // Supprimer une UE
  deleteUe(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Méthode pour récupérer les UE d'un semestre donné
  getUEsBySemestre(semestre: TypeSemestre): Observable<Uejeff[]> {
    const url = `${this.apiUrl}/semestre/${semestre}`;
    return this.http.get<Uejeff[]>(url);
  }


  getUesWithNotesBySemestre(typeSemestre: TypeSemestre): Observable<Uejeff[]> {
    return this.http.get<Uejeff[]>(`${this.baseUrl}/ues/notes/${typeSemestre}`);
  }


  // Obtenir le nombre total d'UE et d'UE par niveau
  getNombreUE(): Observable<any> {
    return this.http.get(`${this.baseUrl}/nombre-ue`);
  }

  // Obtenir le nombre d'UE pour la Première Année
  getUECountPremiereAnnee(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count/premiere-annee`);
  }

  // Obtenir le nombre d'UE pour la Deuxième Année
  getUECountDeuxiemeAnnee(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count/deuxieme-annee`);
  }

  // Obtenir le nombre d'UE pour la Troisième Année
  getUECountTroisiemeAnnee(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count/troisieme-annee`);
  }


}
