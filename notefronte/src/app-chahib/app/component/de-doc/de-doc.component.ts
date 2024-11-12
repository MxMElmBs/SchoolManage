import { Component } from '@angular/core';
import { DocumentService } from '../../services/document-service.service';
import { Document } from '../../models/Document';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DocumentselectService } from '../../services/documentselect.service';
import {  Router } from '@angular/router';
import { EtudiantDetailsDto } from '../../models/EtudiantDetailsDto';
import { LoginService } from '../../../../app-connexion/app/service/login.service';
import { UtilisateurConnect } from '../../models/utilisateur-connect';

@Component({
  selector: 'app-de-doc',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './de-doc.component.html',
  styleUrls: ['./de-doc.component.css']
})
export class DeDocComponent {
  searchQuery: string = '';  // For theme search
  searchTypeDocument: string = '';  // For typeDocument search
  searchNomParcours: string = '';
  documentsByYear: { [year: number]: Document[] } = {}; // Documents groupés par année
  selectedDocument: Document  | undefined;  // Document sélectionné
  etudiantFullName: { [documentId: number]: string } = {};  // Stocker les noms des étudiants par documentId
  etudiantDetails: EtudiantDetailsDto = new EtudiantDetailsDto;
  idUser: number = 0;

  userCo : UtilisateurConnect = new UtilisateurConnect;

  constructor(private documentService: DocumentService, private docsselect: DocumentselectService,
              private router: Router, private authService: LoginService
  ) {}

  ngOnInit() {
    this.idUser = this.authService.getUserId();
    this.getUserCo();

    this.getAllDocuments();  // Charger tous les documents au départ
  }

  getUserCo(){
    this.documentService.getConnectedUser(this.idUser).subscribe(
      (data: UtilisateurConnect) => {
        this.userCo = data;// Logging the response for testing
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

  logout() {
    // Ajoutez ici la logique de déconnexion (par exemple, supprimer les tokens, rediriger vers la page de connexion, etc.)
    this.router.navigate(['/app-gigi/dashboard']);
  }
  // Charger tous les documents
  getAllDocuments() {
    this.documentService.getAllDocumentsGroupedByYear().subscribe(
      (documentsByYear: { [year: number]: Document[] }) => {
        // Grouper les documents par année
        this.documentsByYear = documentsByYear;

        // Initialiser un objet pour stocker les noms des étudiants par documentId
        this.etudiantFullName = {};

        // Parcourir chaque année pour accéder aux documents
        Object.values(documentsByYear).forEach((documents: Document[]) => {
          // Parcourir chaque document pour récupérer les détails de l'étudiant
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
        });
      },
      (error) => {
        console.error('Erreur lors de la récupération des documents', error);
      }
    );
  }


  groupDocumentsByYear(documents: Document[]): { [year: number]: Document[] } {
    return documents.reduce((groupedDocuments: { [year: number]: Document[] }, document: Document) => {
      const year = new Date(document.createdAt).getFullYear();

      // Initialize the array for this year if it doesn't exist
      if (!groupedDocuments[year]) {
        groupedDocuments[year] = [];
      }

      // Add the document to the array for this year
      groupedDocuments[year].push(document);
      return groupedDocuments;
    }, {});  // Start with an empty object
  }

  searchDocuments() {
    this.documentService
      .searchDocuments(this.searchQuery)
      .subscribe(
        (documents: Document[]) => {
          this.documentsByYear = this.groupDocumentsByYear(documents);
        },
        (error) => {
          console.error('Error while searching documents', error);
        }
      );
  }

  // Retourner les années (clés) de l'objet documentsByYear
  getYears(): number[] {
    return Object.keys(this.documentsByYear).map(year => parseInt(year, 10));
  }

  // Méthode pour récupérer le nom complet de l'étudiant associé à un document
  getEtudiantFullName(document: Document): string {
    if (document.etudiant && document.etudiant.nom && document.etudiant.prenom) {
      return `${document.etudiant.prenom} ${document.etudiant.nom}`;
    }
    return 'Nom non disponible';  // Valeur par défaut si l'étudiant n'est pas trouvé
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
}
