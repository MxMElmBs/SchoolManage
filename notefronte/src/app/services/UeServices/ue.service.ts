import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { Ue } from '../../models/ue/ue.model';

@Injectable({
  providedIn: 'root'
})
export class UeService {

  // private apiUrl = 'http://localhost:8070/api/ues';

  // constructor(private http: HttpClient) { }

  // createUE(ue: Ue): Observable<Ue> {
  //   return this.http.post<Ue>(this.apiUrl, ue);
  // }

  // // Obtenir toutes les UE
  // getAllUEs(): Observable<Ue[]> {
  //   return this.http.get<Ue[]>(this.apiUrl);
  // }

  // // Obtenir une UE par ID
  // getUEById(id: number): Observable<Ue> {
  //   return this.http.get<Ue>(`${this.apiUrl}/${id}`);
  // }

  // // Mettre à jour une UE
  // updateUE(id: number, ue: Ue): Observable<Ue> {
  //   return this.http.put<Ue>(`${this.apiUrl}/${id}`, ue);
  // }

  // // Supprimer une UE
  // deleteUE(id: number): Observable<void> {
  //   return this.http.delete<void>(`${this.apiUrl}/${id}`);
  // }
}
