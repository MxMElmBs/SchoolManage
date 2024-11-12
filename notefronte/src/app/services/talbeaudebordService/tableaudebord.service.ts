import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TableaudebordService {

  private apiUrl = 'http://localhost:8060/api/auth/de';

  constructor(private http: HttpClient) { }

  // Taux de réussite par UE
  getTauxReussiteParUe(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/taux-reussite-ue`);
  }

  // Distribution des notes
  getDistributionDesNotes(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/distribution-notes`);
  }


}
