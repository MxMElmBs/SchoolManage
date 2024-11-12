import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Paiement } from '../MODELS/paiement';

@Injectable({
  providedIn: 'root'
})
export class PaiementService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable/';
  private apiAjoutUrl = 'http://localhost:8060/api/auth/comptable/ajout_paiement';
  private baseUrl = 'http://localhost:8060/api/auth/comptable';
  private apiRenvoiUrl = 'http://localhost:8060/api/auth/comptable/etudiants/en-cours';
  private apiPaiUrl = 'http://localhost:8060/api/auth/comptable/paiement';

  constructor(private http: HttpClient) {}



// endpoint pour récupérer les étudiants avec les statuts spécifiés
getEtudiantsParStatuts(): Observable<any[]> {
  return this.http.get<any[]>(this.apiUrl + '/par-statut');
}

ajouterPaiement(data: Paiement): Observable<Paiement> {
  return this.http.post<Paiement>(this.apiAjoutUrl, data);
}

updatePaiement(nom: string, prenom: string, montantActuel: number, datePaiement: string): Observable<any> {
  const url = `${this.baseUrl}/update_paiement?nomEtudiant=${nom}&prenomEtudiant=${prenom}`;
  return this.http.put(url, { montantActuel, datePaiement });
}
getEtudiantsEnCours(): Observable<Paiement[]> {
  return this.http.get<Paiement[]>(this.apiRenvoiUrl);
}

getPaiementByEtudiantMatricule(matricule: string): Observable<Paiement> {
  return this.http.get<Paiement>(`${this.apiPaiUrl}/${matricule}`);
}

}
