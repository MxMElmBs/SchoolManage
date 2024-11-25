import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Document } from '../models/Document';  // Import du modèle Document
import { CreateDocumentDto } from '../models/CreateDocumentDto';  // Import du DTO CreateDocumentDto
import { Professeur } from '../models/Professeur';
import { FormulaireVerification } from '../models/FormulaireVerification';
import { EtudiantDetailsDto } from '../models/EtudiantDetailsDto';
import { EtudiantDocDto } from '../models/etudiant-doc-dto';
import { UtilisateurConnect } from '../models/utilisateur-connect';

@Injectable({
  providedIn: 'root'  // Le service est fourni à l'échelle de l'application
})
export class DocumentService {
  private readonly apiUrl = 'http://localhost:8060/api/auth/documents';  // L'URL de base de votre API Spring Boot

  constructor(private http: HttpClient) {}

  // Méthode pour ajouter un nouveau document
  addDocument(documentData: CreateDocumentDto): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/add`, documentData, { headers });
  }


  getEtudiantByUserID(userId: number): Observable<EtudiantDocDto> {
    return this.http.get<EtudiantDocDto>(`http://localhost:8060/api/auth/etudiant/getetudiant/${userId}`);
  }

   // Méthode pour obtenir les documents d'un étudiant via son ID utilisateur
   getDocumentsByUserId(userId: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/documents-by-user/${userId}`);
  }

  // Méthode pour obtenir les détails d'un étudiant via son ID utilisateur
  getEtudiantByUserId(userId: number): Observable<EtudiantDocDto> {
    return this.http.get<EtudiantDocDto>(`${this.apiUrl}/etudiant-by-user/${userId}`);
  }

  getConnectedUser(userId: number): Observable<UtilisateurConnect> {
    return this.http.get<UtilisateurConnect>(`${this.apiUrl}/connect/${userId}`);
  }


  // Méthode pour vérifier si un étudiant a déjà un document du même type
  checkDocumentExists(etudiantId: number, documentType: string): Observable<boolean> {
    const params = new HttpParams()
      .set('etudiantId', etudiantId.toString())
      .set('typeDocument', documentType);
    return this.http.get<boolean>(`${this.apiUrl}/checkDocumentExists`, { params });
  }

  // Méthode pour vérifier si un thème existe déjà (en excluant l'ID d'un document donné)
  checkThemeExists(theme: string, documentId?: number): Observable<boolean> {
    let params = new HttpParams().set('theme', theme);

    if (documentId) {
      params = params.set('excludeId', documentId.toString());  // Passer l'ID du document pour l'exclure
    }

    return this.http.get<boolean>(`${this.apiUrl}/checkTheme`, { params });
  }

  // Méthode pour rechercher des documents par thème, type de document, et parcours
  searchDocuments(theme: string): Observable<Document[]> {
    let params = new HttpParams().set('theme', theme);
    return this.http.get<Document[]>(`${this.apiUrl}/search`, { params });
  }

  // Méthode pour obtenir tous les documents
  getAllDocuments(): Observable<Document[]> {
    return this.http.get<Document[]>(this.apiUrl);
  }

  // Méthode pour obtenir un document par son ID
  getDocumentById(documentId: number): Observable<Document> {
    return this.http.get<Document>(`${this.apiUrl}/documentby/${documentId}`);
  }


 // Méthode pour obtenir tous les professeurs (URL corrigée)
 //getAllProfesseurs(): Observable<Professeur[]> {
  //return this.http.get<Professeur[]>(`http://localhost:8060/api/auth/professeur/fordocs`);
  //}

  // Méthode pour obtenir tous les professeurs
  getAllProfesseurs(): Observable<Professeur[]> {
    return this.http.get<Professeur[]>(`http://localhost:8060/api/auth/de/all-prof`);
  }

  // Méthode pour obtenir les documents d'un étudiant spécifique
  getDocumentsByEtudiantId(etudiantId: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/documentbyetudiant/${etudiantId}`);
  }

  // Méthode pour vérifier la similarité et mettre à jour le document
  verifyAndUpdateDocument(documentId: number, formVerification: FormulaireVerification): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/verifyformulaire/${documentId}`, formVerification, { headers });
  }

  // Méthode pour sauvegarder un document
  saveDocument(documentData: Partial<Document>): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/add`, documentData, { headers });
  }

  // Méthode pour vérifier la similarité et mettre à jour le document avec un fichier
  verifyAndUpdateDocumentWithFile(
    documentId: number,
    formVerification: FormulaireVerification,
    file?: File
  ): Observable<any> {
    const formData = new FormData();

    // Ajouter les champs du formulaire dans le FormData
    formData.append('documentId', documentId.toString());
    if (formVerification.introduction) {
      formData.append('introduction', formVerification.introduction);
    }
    if (formVerification.Problematique) {
      formData.append('Problematique', formVerification.Problematique);
    }
    if (formVerification.Etude_critique_existant) {
      formData.append('Etude_critique_existant', formVerification.Etude_critique_existant);
    }
    if (formVerification.resumeDoc) {
      formData.append('resumeDoc', formVerification.resumeDoc);
    }
    if (formVerification.conclusion) {
      formData.append('conclusion', formVerification.conclusion);
    }

    // Si un fichier est présent, on l'ajoute dans le FormData
    if (file) {
      formData.append('file', file, file.name);
    }

    // Effectuer la requête POST multipart/form-data
    return this.http.post(`${this.apiUrl}/verify-update-document`, formData, { responseType: 'text' });
  }

  // Méthode pour mettre à jour un document
  updateDocument(documentId: number, updatedDocument: CreateDocumentDto): Observable<any> {
    const url = `${this.apiUrl}/updatedocumentinfo/${documentId}`;
    return this.http.put(url, updatedDocument, { responseType: 'text' });  // Use PUT request to update document
  }

  // Méthode pour obtenir les détails d'un étudiant
  getEtudiantDetails(documentId: number): Observable<EtudiantDetailsDto> {
    return this.http.get<EtudiantDetailsDto>(`${this.apiUrl}/${documentId}/details`);
  }

  // Méthode pour récupérer les documents d'un professeur spécifique
  getDocumentsByProfesseurId(professeurId: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/documents-by-professeur/${professeurId}`);
  }

  // Méthode pour récupérer tous les documents groupés par année
  getAllDocumentsGroupedByYear(): Observable<{ [year: number]: Document[] }> {
    return this.http.get<{ [year: number]: Document[] }>(`${this.apiUrl}/all`);
  }


  getProfesseurByDocumentId(documentId: number): Observable<Professeur> {
    return this.http.get<Professeur>(`http://localhost:8060/api/auth/documents/${documentId}/professeur`);
  }

   // Appelle l'API pour récupérer le nom complet de l'étudiant par documentId
   getEtudiantFullName(documentId: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/document/${documentId}/etudiant-fullname`);
  }

  // Méthode pour récupérer le professeur associé à un document via l'ID du document
  getProfesseurByDocumentID(documentId: number): Observable<Professeur> {
    return this.http.get<Professeur>(`${this.apiUrl}/${documentId}/professeur`);
  }

}
