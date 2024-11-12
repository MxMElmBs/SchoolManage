import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Paiement } from '../MODELS/paiement';

@Injectable({
  providedIn: 'root'
})
export class EcheanceService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable/rappel'; // L'URL de ton API backend

  constructor(private http: HttpClient) { }

  getRappelsAVenir(): Observable<Paiement[]> { // Assuming Paiement is the DTO
    return this.http.get<Paiement[]>(this.apiUrl);
  }



  getNombreRappelsAVenir(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`); // Appel à l'endpoint pour le nombre de paiements
  }

}
