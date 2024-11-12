import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Etudiant } from '../MODELS/etudiant';
import { Filiere } from '../MODELS/filiers';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  private apiUrl = 'http://localhost:8060/api/auth/comptable';
  private apiPartiAttenteUrl = 'http://localhost:8060/api/auth/comptable/parti-attente';
  private apiTpayeeUrl = 'http://localhost:8060/api/auth/comptable/Tpayee';
  private apiAddUrl = 'http://localhost:8060/api/auth/comptable/ajout_etudiant';
  private apiAllfilUrl = 'http://localhost:8060/api/auth/comptable/filiere';
  private baseUrl = 'http://localhost:8060/api/auth/comptable/ajout_paiement';
  private apiModUrl = 'http://localhost:8060/api/auth/comptable/update_etudiant';
  private baseFilUrl = 'http://localhost:8060/api/auth/comptable';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }
  ///////////////////////////////////////////////////////////////////
  //////    tableau de bord   /////////////
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getPartiAttente(): Observable<number> {
    return this.http.get<number>(this.apiPartiAttenteUrl);
  }

  getTpayee(): Observable<number> {
    return this.http.get<number>(this.apiTpayeeUrl);
  }

  ///////////////////////////////////////////////////////////////////
  //////    ETUDIANTS   /////////////
  // Ajouter un nouvel étudiant
  ajouterEtudiant(etudiant: Etudiant): Observable<Etudiant> {
    return this.http.post<Etudiant>(`${this.apiAddUrl}/ajout_etudiant`, etudiant);
  }

  // Mettre à jour un étudiant existant
 updateEtudiant(matricule: string, etudiant: Etudiant): Observable<Etudiant> {
    const url = `${this.apiUrl}/${matricule}`;
    return this.http.put<Etudiant>(url, etudiant);
  }
  // Supprimer un étudiant par ID
  deleteEtudiant(matricule: string): Observable<void> {
    return this.http.delete<void>(`http://localhost:8060/api/auth/comptable/etudiant/${matricule}`);
}

  // Récupérer tous les étudiants
  getAllEtudiants(): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.apiUrl}/etudiant`);
  }

  // Récupérer un étudiant par ID
  getEtudiantById(id: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.apiUrl}/etudiant/${id}`);
  }
  getEtudiantByMatricule(matricule: string) {
    return this.http.get<Etudiant>(`http://localhost:8060/api/auth/comptable/etudiant/${matricule}`);
  }

  // Rechercher des étudiants par parcours et niveau
  searchEtudiants(parcoursNom: string, niveau: string): Observable<Etudiant[]> {
    let params = new HttpParams()
      .set('parcoursNom', parcoursNom)
      .set('niveau', niveau);

    return this.http.get<Etudiant[]>(`${this.apiUrl}/search`, { params });
  }
  ///le montantFinal
  getMontantFinal(statutBoursier: string, nomParcours: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/calculMontantScolarite?statut=${statutBoursier}&parcours=${nomParcours}`);
  }


  // Méthode pour récupérer les étudiants par parcours et niveau
  getEtudiantsByParcoursAndNiveau(parcours: string, niveau: string): Observable<any[]> {
    const url = `${this.apiUrl}/${parcours}/${niveau}`;
    return this.http.get<any[]>(url);
  }

  /////Methode pour Chercher etudiant nom et prenom
  searchEtudiantsByNomAndPrenom(nom: string, prenom: string): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.apiUrl}/search?nom=${nom}&prenom=${prenom}`);
  }


  /////Methode pour get les rappels a Venir
  getRappel(): Observable<any> {
    return this.http.get<Etudiant[]>(`${this.apiUrl}/rappel`);
  }
  ///////////////////////////////////////////////////////////////////
  //////    PAIEMENTS   /////////////
  /////Methode pour get tous les filieres pour paiement
  getAllFilieres(): Observable<Filiere[]> {
    return this.http.get<Filiere[]>(this.apiAllfilUrl);
  }
  /////Methode pour post
  addPaiement(paiement: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, paiement);
  }
  searchByFiliere(parcoursId: number, nomFiliere: string, niveauEtude: string): Observable<any> {
    const url = `${this.baseFilUrl}/searchbyfiliere?parcoursId=${parcoursId}&nomFiliere=${nomFiliere}&niveauEtude=${niveauEtude}`;
    return this.http.get(url);
  }

  //////    impression   /////////////
  getEtudiantsByFiliereAndNiveau(nomFiliere: string, niveauEtude: string): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.baseUrl}/filiere/${nomFiliere}/niveau/${niveauEtude}`);
  }

}
