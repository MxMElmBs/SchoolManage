import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SaisieNoteDto } from '../../models/SaisieNote/saisie-note-dto';
import { Uejeff } from '../../models/uejeff/uejeff';
import { NoteEtudiantDto } from '../../models/NoteEtudiantDto/note-etudiant-dto';
import { TypeNote } from '../../models/enums/type-note';
import { NoteDto } from '../../models/NoteDto/note-dto';

@Injectable({
  providedIn: 'root'
})
export class NoteService {

  private apiUrl = 'http://localhost:8060/api/auth/de/notes/enregistrer';
  private baseUrl = 'http://localhost:8060/api/auth/de';


  constructor(private http: HttpClient) { }

  // Méthode pour enregistrer les notes
  enregistrerNotes(saisieListNotes: SaisieNoteDto[]): Observable<any> {
    return this.http.post(this.apiUrl, saisieListNotes);
  }

  // Obtenir les notes des étudiants pour une UE et un type de note
  getNotesEtudiantsParUEEtTypeNote(ueId: number, typeNote: TypeNote): Observable<NoteEtudiantDto[]> {
    return this.http.get<NoteEtudiantDto[]>(`${this.baseUrl}/ues/${ueId}/notes/${typeNote}`);
  }

  // Modifier les notes pour une UE
  // modifierNotes(notes: NoteEtudiantDto[]): Observable<{ message: string }> {
    modifierNotes(saisieNotes: SaisieNoteDto[]): Observable<{ message: string }> {
      return this.http.put<{ message: string }>(`${this.baseUrl}/notes/modifier`, saisieNotes);
    }

  // Supprimer les notes pour une UE et un type de note
  supprimerNotes(ueId: number, typeNote: TypeNote): Observable<any> {
    return this.http.delete(`${this.baseUrl}/notes/${ueId}/${typeNote}`);
  }
        // Etudiant
  // Récupérer les notes d'un étudiant par ID
  getNotesByEtudiant(id: number): Observable<NoteDto[]> {
    return this.http.get<NoteDto[]>(`${this.baseUrl}/noteetudiant/${id}`);
  }

  // Récupérer une note par son ID
  getNoteById(noteId: number): Observable<NoteDto> {
    return this.http.get<NoteDto>(`${this.baseUrl}/note/${noteId}`);
  }

  // Modifier une note
  updateNoteEtudiant(noteId: number, noteDto: NoteDto): Observable<NoteDto> {
    return this.http.put<NoteDto>(`${this.baseUrl}/note/${noteId}`, noteDto);
  }

  // Supprimer une note
  deleteNote(noteId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/note/${noteId}`);
  }


}
