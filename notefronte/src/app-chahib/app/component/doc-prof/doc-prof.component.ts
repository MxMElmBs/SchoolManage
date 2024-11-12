import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document-service.service';
import { Document } from '../../models/Document';
import { Professeur } from '../../models/Professeur'; // Importer le modèle Professeur
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http'; // Importer HttpClient pour l'appel API
import Swal from 'sweetalert2';  // Pour les notifications
import { DocumentselectService } from '../../services/documentselect.service';
import { Router } from '@angular/router';
import { Etudiant } from '../../models/Etudiant';
import { Observable } from 'rxjs';  // Importer Observable
import { EtudiantDetailsDto } from '../../models/EtudiantDetailsDto';
import { LoginService } from '../../../../app-connexion/app/service/login.service';
import { UtilisateurConnect } from '../../models/utilisateur-connect';

@Component({
  selector: 'app-doc-prof',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doc-prof.component.html',
  styleUrls: ['./doc-prof.component.css']
})
export class DocProfComponent implements OnInit {
  searchQuery: string = '';  // Pour capturer la recherche par thème
  documentsByYear: { [year: number]: Document[] } = {};  // Documents groupés par année
  professeurId: number | null = null;  // ID du professeur, initialisé à null
  selectedProfesseur: Professeur | null = null;  // Stocker les données du professeur
  etudiantDetails: EtudiantDetailsDto = new EtudiantDetailsDto;
  etudiantFullName: { [documentId: number]: string } = {};  // Stocker les noms des étudiants par documentId
  selectedDocument: Document  | undefined;  // Document sélectionné
  userConect : UtilisateurConnect = new UtilisateurConnect;
  idUser: number = 1;



  constructor(
    private documentService: DocumentService,
    private http: HttpClient ,
    private router: Router,
    private docsselect: DocumentselectService,
    private userCo: LoginService
  ) {}

  ngOnInit() {
    this.idUser = this.userCo.getUserId();
    this.getUserCo();
    console.log("Prof id connect : ", this.idUser) // Essayer de récupérer professeurId depuis localStorage
    if (!this.idUser) {
      this.getProfesseurByUserId();
      this.getDocumentsByProfesseur();  // Si on a déjà le professeurId, récupérer les documents
    }
    console.log(this.selectedProfesseur)
  }


  getUserCo(){
    this.documentService.getConnectedUser(this.idUser).subscribe(
      (data: UtilisateurConnect) => {
        this.userConect = data;// Logging the response for testing
        console.log("Prof connect: ", this.userConect)
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }
  // Récupérer les informations du professeur en fonction de l'ID utilisateur
  getProfesseurByUserId(): void {
    const userId = this.idUser;

    if (userId) {
      // Assurez-vous que l'URL est correcte
      this.http.get<Professeur>(`http://localhost:8060/api/auth/professeur/professeurInfo/${userId}`)
        .subscribe({
          next: (professeurDto) => {
            this.selectedProfesseur = professeurDto;
            this.professeurId = professeurDto.professeurId;
          },
          error: (error) => {
            console.error('Erreur lors de la récupération des données du professeur', error);
            Swal.fire('Erreur', 'Impossible de récupérer les informations du professeur.', 'error');
          }
        });

    } else {
      console.error('Aucun userId trouvé dans localStorage');
      Swal.fire('Erreur', 'Aucun utilisateur connecté trouvé.', 'error');
    }
  }

  // Charger tous les documents d'un professeur
  getDocumentsByProfesseur() {
    if (this.professeurId !== null) {
      this.documentService.getDocumentsByProfesseurId(this.professeurId).subscribe(
        (documents: Document[]) => {
          this.documentsByYear = this.groupDocumentsByYear(documents);  // Grouper les documents par année

          // Initialiser un objet pour stocker les noms des étudiants par documentId
          this.etudiantFullName = {};

          // Récupérer le nom de l'étudiant pour chaque document
          documents.forEach((document: Document) => {
            this.documentService.getEtudiantDetails(document.id)
            .subscribe(
              (data: EtudiantDetailsDto) => {
                // Stocker le nom complet de l'étudiant par documentId
                this.etudiantFullName[document.id] = data.nom + ' ' + data.prenom;
                console.log(`Détails de l'étudiant pour le document ${document.id} récupérés:`, this.etudiantFullName[document.id]);
              },
              (error: any) => {
                console.error(`Erreur lors de la récupération des détails du document ${document.id} :`, error);
              }
            );
          });
        },
        (error) => {
          console.error('Erreur lors de la récupération des documents', error);
        }
      );
    }
  }


  // Grouper les documents par année
  groupDocumentsByYear(documents: Document[]): { [year: number]: Document[] } {
    return documents.reduce((groupedDocuments, document) => {
      const date = new Date(document.createdAt);
      const year = date.getFullYear();

      if (!groupedDocuments[year]) {
        groupedDocuments[year] = [];
      }
      groupedDocuments[year].push(document);
      return groupedDocuments;
    }, {} as { [year: number]: Document[] });
  }



  // Méthode pour effectuer la recherche de documents
  searchDocuments() {
    this.documentService.searchDocuments(this.searchQuery).subscribe(
      (documents: Document[]) => {
        this.documentsByYear = this.groupDocumentsByYear(documents);
      },
      (error) => {
        console.error('Erreur lors de la recherche de documents', error);
      }
    );
  }

  // Retourner les années (clés) de l'objet documentsByYear
  getYears(): number[] {
    return Object.keys(this.documentsByYear).map(year => parseInt(year, 10));
  }

  getEtudiantDetailsK(docs: Document): void {
    console.log(docs)
    if (docs.id) {
      this.documentService.getEtudiantDetails(docs.id)
        .subscribe(
          (data: EtudiantDetailsDto) => {
            this.etudiantDetails = data;
            console.log('Détails de l\'étudiant récupérés:', this.etudiantDetails);
          },
          (error: any) => {
            console.error('Erreur lors de la récupération des détails du document :', error);
          }
        );
    }
  }

  selectDocument(document: Document) {
    console.log('Document sélectionné :', document);
    this.docsselect.setSelectedDocument(document);  // Stocker le document dans le service
    this.selectedDocument = document;  // Afficher les détails du document
    this.getEtudiantDetailsK(this.selectedDocument);
  }

  // Méthode pour télécharger un document
  downloadDocument() {
    if (this.etudiantDetails && this.etudiantDetails.documentUrl) {
      console.log('Document URL:', this.etudiantDetails.documentUrl);

      // Crée un élément <a> temporaire pour télécharger le document
      const link = document.createElement('a');
      link.href = this.etudiantDetails.documentUrl;
      link.target = '_blank'; // Ouvre dans un nouvel onglet
      link.download = this.etudiantDetails.documentUrl.split('/').pop() || 'document';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);  // Nettoyage après le téléchargement
    } else {
      console.error('URL du document non disponible');
    }
  }

  // Méthode pour fermer le pop-up et revenir à la liste des documents
  closeDocumentDetails() {
    this.selectedDocument = undefined;  // Efface le document sélectionné pour fermer le pop-up
  }
  logout() {
    // Ajoutez ici la logique de déconnexion (par exemple, supprimer les tokens, rediriger vers la page de connexion, etc.)
    this.router.navigate(['/app-gigi/professeur/dashboard-prof']);
  }
}
