import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Filiere } from '../../models/filiere/filiere';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FiliereService {

  private apiUrl = 'http://localhost:8060/api/auth/de/toutefiliere'

  constructor(private http: HttpClient) { }

  getFilieres(): Observable<Filiere[]> {
    return this.http.get<Filiere[]>(this.apiUrl);
  }
}
