import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DocumentService } from '../../services/document-service.service';
import { DocumentselectService } from '../../services/documentselect.service';
import { Document } from '../../models/Document';
import { LoginService } from '../../../../app-connexion/app/service/login.service';
import { EtudiantDocDto } from '../../models/etudiant-doc-dto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http'; // Import HttpClient pour les appels API
import { EtudiantConnect } from '../../../../app-students-interfaces/app/models/EtudiantConnect';
import { IEtudiantServicesService } from '../../../../app-students-interfaces/app/services/ietudiant-services.service';
import { LocalStorageService } from '../../services/local-storage.service';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './homechahib.component.html',
  styleUrls: ['./homechahib.component.css'],
  providers: [LocalStorageService]
})
export class HomechahibComponent implements OnInit {
  documents: Document[] = [];  // Liste des documents
  searchQuery: string = '';  // Ajout de la propriété searchQuery pour la recherche
  etudiantId: number = 0;  // ID de l'étudiant
  etudiant: EtudiantDocDto = new EtudiantDocDto(0, '', '', '', '');
  idUser: number = 0;  // ID de l'utilisateur connecté
  student : EtudiantConnect = new EtudiantConnect(0, '', '', '', '', '', '', '','','');
  idUsers: number = 0;
  constructor(
    private router: Router,
    private documentService: DocumentService,
    private docsselect: DocumentselectService,
    private etudiantS: IEtudiantServicesService,
    private userConnect: LoginService,
    private http: HttpClient,  // Injecter HttpClient ici
    private studentSave: LocalStorageService
  ) { }

  ngOnInit(): void {
    // Récupérer l'ID utilisateur connecté depuis localStorage
    this.idUsers = this.userConnect.getUserId();
    this.getEtudiant();
    console.log('Id recupere est : ',this.idUsers)



    if (this.idUsers > 0) {
      this.getEtudiantInfo();
      this.getDocumentsByEtudiant();
    } else {
      console.error('ID utilisateur non valide ou non trouvé');
    }
  }

  // Méthode pour récupérer les informations de l'étudiant via son ID utilisateur
  getEtudiantInfo(): void {
    console.log('Appel de getEtudiantInfo avec idUser:', this.idUsers);

    this.http.get<any>(`http://localhost:8060/api/auth/etudiant/etudiantInfo/${this.idUsers}`).subscribe(
      (data: any) => {
        console.log('Données étudiant reçues:', data);
        this.etudiant = data;
        this.etudiantId = data.etudiantId || 0;  // Récupérer l'ID de l'étudiant directement
        console.log('etudiantId:', this.etudiantId);

        if (this.etudiantId > 0) {
          localStorage.setItem('etudiantId', this.etudiantId.toString());
          this.studentSave.setStudentId(this.etudiantId);
          console.log('etudiantId stocké dans localStorage:', localStorage.getItem('etudiantId'));
          this.getDocumentsByEtudiant();  // Appel de la méthode pour récupérer les documents
        } else {
          console.warn('etudiantId est undefined ou null, la méthode getDocumentsByEtudiant() ne sera pas appelée');
        }

      },
      (error) => {
        console.error('Erreur lors de la récupération des données de l\'étudiant', error);
      }
    );
  }

  // Méthode pour ajouter un document
  addDocument(): void {
    this.router.navigate(['/app-chahib/control']);
  }

  // Méthode pour rechercher des documents
  searchDocuments(): void {
    if (this.searchQuery.trim()) {
      this.documentService.searchDocuments(this.searchQuery).subscribe(
        (response: Document[]) => {
          this.documents = response;
        },
        (error) => {
          console.error('Erreur lors de la recherche des documents', error);
        }
      );
    }
  }

  getEtudiant(): void {
    this.etudiantS.getConnectedEtudiant(this.idUsers).subscribe(
      (data: EtudiantConnect) => {
        this.student = data;// Logging the response for testing
        console.log('student :',  this.student);
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

  // Méthode pour récupérer les documents d'un étudiant spécifique
  getDocumentsByEtudiant(): void {
    if (this.etudiantId > 0) {
      this.documentService.getDocumentsByEtudiantId(this.etudiantId).subscribe(
        (response: Document[]) => {
          this.documents = response;
          console.log('Documents récupérés:', this.documents);
        },
        (error) => {
          console.error('Erreur lors de la récupération des documents de l\'étudiant', error);
        }
      );
    } else {
      console.warn('Aucun ID étudiant valide disponible');
    }
  }

  // Sélectionner un document et rediriger vers une autre page
  selectDocument(document: Document): void {
    this.docsselect.setSelectedDocument(document);
    this.router.navigate(['/app-chahib/controlemore', document.id]);  // document.id est l'ID du document sélectionné
  }

  logout() {
    // Ajoutez ici la logique de déconnexion (par exemple, supprimer les tokens, rediriger vers la page de connexion, etc.)
    this.router.navigate(['/app-etudiant-interface/studentsisetting']);
  }

}
