import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Filiere } from '../MODELS/filiers';
import { Etudiant } from '../MODELS/etudiant';

@Injectable({
  providedIn: 'root'
})
export class FiliersService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable'
  constructor(private http: HttpClient) { }



  getFiliereByParcours(parcoursId: number): Observable<Filiere[]> {
    return this.http.get<Filiere[]>(`${this.apiUrl}/filiere?parcoursId=${parcoursId}`);
  }


   // Récupérer les filières par parcoursId
 getFilieresByParcours(parcoursId: number): Observable<Filiere[]> {
  return this.http.get<Filiere[]>(`${this.apiUrl}/filieres/parcours/${parcoursId}`);
}

// Récupérer les étudiants par filière et niveau
getEtudiantsByFiliereAndNiveau(nomFiliere: string, niveauEtude: string): Observable<Etudiant[]> {
  return this.http.get<Etudiant[]>(`${this.apiUrl}/filiere/${nomFiliere}/niveau/${niveauEtude}`);
}


getFilieresByParcoursId(parcoursId: number): Observable<Filiere[]> {
  return this.http.get<Filiere[]>(`${this.apiUrl}/${parcoursId}`);
}

}
