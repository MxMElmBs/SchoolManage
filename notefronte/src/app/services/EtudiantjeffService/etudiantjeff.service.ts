import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TypeSemestre } from '../../models/enums/type-semestre';
import { Observable } from 'rxjs';
import { Etudiantjeff } from '../../models/etudiantjeff/etudiantjeff';
import { NoteEtudiantDto } from '../../models/NoteEtudiantDto/note-etudiant-dto';
import { EtudiantDto } from '../../models/EtudiantDto/etudiant-dto';
import { EtudiantMoyenneDto } from '../../models/EtudiantMoyenneDto/etudiant-moyenne-dto';

@Injectable({
  providedIn: 'root'
})
export class EtudiantjeffService {

  private apiUrl = 'http://localhost:8060/api/auth/de/etudiants/semestre';
  private baseUrl = 'http://localhost:8060/api/auth/de/noteetudiant/with-notes'
  private cutUrl = 'http://localhost:8060/api/auth/de'

  constructor(private http: HttpClient) { }

  // Méthode pour récupérer les étudiants d'un niveau en fonction du semestre
  getEtudiantsBySemestre(semestre: TypeSemestre): Observable<Etudiantjeff[]> {
    const url = `${this.apiUrl}/${semestre}`;
    return this.http.get<Etudiantjeff[]>(url);
  }

  // Méthode pour récuprér la liste de tous les étudiants qui ont reçu une note
  getEtudiantsWithNotes(): Observable<Etudiantjeff[]> {
    return this.http.get<Etudiantjeff[]>(this.baseUrl);
  }

  // Bulletin
  getEtudiantsByNiveau(niveau: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.cutUrl}/bulletin/niveau/${niveau}`);
  }

  // Récupérer les informations d'un étudiant par son ID
  getEtudiantById(id: number): Observable<EtudiantDto> {
    return this.http.get<EtudiantDto>(`${this.cutUrl}/bulletin/${id}`);
  }

  // Obtenir le nombre total d'étudiants et d'étudiants par niveau
  getNombreEtudiants(): Observable<any> {
    return this.http.get(`${this.cutUrl}/nombre-etudiants`);
  }

  getTop5Etudiants(): Observable<EtudiantMoyenneDto[]> {
    return this.http.get<EtudiantMoyenneDto[]>('http://localhost:203/api/de/etudiants/top5');
  }


}
