import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DocumentService } from '../../services/document-service.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CreateDocumentDto } from '../../models/CreateDocumentDto';
import { Professeur } from '../../models/Professeur';
import { DocumentselectService } from '../../services/documentselect.service';
import { Document } from '../../models/Document';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-control',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './update-control.component.html',
  styleUrls: ['./update-control.component.css']
})
export class UpdateControlComponent implements OnInit {
  theme: string = '';
  selectedProfesseur: Professeur | null = null;
  typeDocument: string = '';  
  professeurs: Professeur[] = [];
  documentId: number = 1;  
  isThemeValid: boolean = true;
  isLoading: boolean = false;
  errorMessage: string | null = null;
  selectDocs: Document = new Document();      
  
  constructor(
    private documentService: DocumentService,
    private router: Router,
    private docSelect: DocumentselectService
  ) {}

  ngOnInit(): void {
    this.selectDocs = this.docSelect.getSelectedDocument();
    this.documentId = this.selectDocs.id;
    this.loadDocument();
    this.loadProfesseurs();
  }

  // Charger les informations du document existant
  loadDocument() {
    this.documentService.getDocumentById(this.documentId).subscribe(
      (document) => {
        this.theme = document.theme;
        this.selectedProfesseur = document.professeur;
        this.typeDocument = document.typeDocument;
      },
      (error: any) => {
        this.isLoading = false;
        this.showErrorPopup('Erreur lors de la récupération du document');
      }
    );
  }

  // Charger la liste des professeurs
  loadProfesseurs() {
    this.isLoading = true;  
    this.documentService.getAllProfesseurs().subscribe(
      (data: Professeur[]) => {
        this.professeurs = data;
        
        this.isLoading = false;  
      },
      (error: any) => {
        this.isLoading = false;
        this.showErrorPopup('Erreur lors de la récupération des professeurs');
      }
    );
  }

  // Validation du thème
  validateTheme() {
    this.isThemeValid = this.theme.trim().length > 0;
  }

  // Méthode pour soumettre les modifications après vérification du thème
  handleUpdate() {
    this.validateTheme();
    if (!this.isThemeValid) {
      this.showErrorPopup('Thème non valide');
      return;
    }

    this.isLoading = true;

    // Vérifier si le thème est similaire à un autre document, en excluant ce document (ID)
    this.documentService.checkThemeExists(this.theme, this.documentId).subscribe(
      (themeExists: boolean) => {
        if (themeExists) {
          this.isLoading = false;
          this.showErrorPopup('Un document avec un thème similaire existe déjà.');
        } else {
          this.updateDocument();  // Si pas de similarité, poursuivre la mise à jour
        }
      },
      (error: any) => {
        this.isLoading = false;
        this.showErrorPopup('Erreur lors de la vérification de la similarité du thème');
      }
    );
  }

  // Méthode pour mettre à jour le document
  updateDocument() {
    const updatedDocument: CreateDocumentDto = {
      typeDocument: this.typeDocument,
      theme: this.theme,
      etudiantId: this.selectDocs.etudiant?.id ?? 0,
      professeurId: this.selectedProfesseur?.professeurId ?? 0
    };

    this.documentService.updateDocument(this.documentId, updatedDocument).subscribe(
      (response: string) => {
        this.isLoading = false;
        this.showSuccessPopup('Document mis à jour avec succès !');
      },
      (error: any) => {
        this.isLoading = false;

        // Vérification du message d'erreur pour la similarité du thème
        if (error.error && error.error.includes('trop similaire à un document existant')) {
          this.showErrorPopup('Le nouveau thème est trop similaire à un document existant.');
        } else {
          this.showErrorPopup('Erreur lors de la mise à jour du document.');
        }
      }
    );
  }

  // Pop-up de succès
  private showSuccessPopup(successMessage: string): void {
    Swal.fire({
      icon: 'success',
      title: 'Succès',
      text: successMessage,
      confirmButtonText: 'OK'
    }).then(() => {
      this.router.navigate(['/app-chahib/home']);
    });
  }

  // Pop-up d'erreur avec message spécifique pour le thème
  private showErrorPopup(errorMessage: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Erreur',
      text: errorMessage,
      confirmButtonText: 'OK'
    });
  }
}
