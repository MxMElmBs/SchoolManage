import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Paiement } from '../MODELS/paiement';


// Définir l'interface pour le modèle de paiement
interface Paie {
  etudiantId: number;
  etudiantNom: string;
  etudiantPrenom: string;
  etudiantMatricule: string;
  filiereNom: string;
  parcoursNom: string;
  reductionMontantFinal: number;
  niveauEtude: string;
  montantDejaPaye: number;
  typeModalite: string;
  datePaiement: string;
  montantActuel: number;
  resteEcolage: number;
  montantAChanger: number;
  echeances: Array<{
    id: number;
    montantParEcheance: number;
    dateEcheance: string;
    statut: string;
    resteSurEcheance: number;
    dateEnvoi: string;
  }>;
  statutScolarite: string;
  tuteurMail: string;
}
@Injectable({
  providedIn: 'root'
})
export class PaieService {

  private apiUrl = 'http://localhost:8060/api/auth/comptable/ajout_paiement';

  constructor(private http: HttpClient) {}

  // Fonction pour envoyer les données de paiement
  ajouterPaiement(paie: Paie): Observable<Paie> {
    return this.http.post<Paie>(this.apiUrl, paie);
  }
}
