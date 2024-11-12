import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';  // Assurez-vous que FormsModule est importé
import { CommonModule } from '@angular/common';
import { DocumentService } from '../../services/document-service.service';  // Le service pour obtenir le document
import { Document } from '../../models/Document';  // Import du modèle Document
import { FormulaireVerification } from '../../models/FormulaireVerification';  // Import du modèle FormulaireVerification
import Swal from 'sweetalert2';
import { Router } from '@angular/router';  // Importer le Router
import { DocumentselectService } from '../../services/documentselect.service';

@Component({
  selector: 'app-formulaire',
  standalone: true,
  imports: [FormsModule, CommonModule],  // FormsModule et CommonModule doivent être inclus ici
  templateUrl: './formulaire.component.html',
  styleUrls: ['./formulaire.component.css']
})
export class FormulaireComponent implements OnInit {
  theme: string = '';
  introduction: string = '';
  Problematique: string = '';  
  Etude_critique_existant: string = '';  
  resumeDoc: string = '';
  conclusion: string = '';
  documentId: number = 1;
  selectedFile: File | null = null;

  similarityResult: string = '';  // Pour afficher le résultat de la vérification
  errorMessage: string = '';  // Pour stocker et afficher les erreurs du backend
  isThemeReadOnly: boolean = true;  // Pour rendre le champ de thème en lecture seule
  isLoading: boolean = false;  // Indicateur pour montrer le processus en cours

  // Injecter le Router dans le constructeur
  constructor(private documentService: DocumentService, 
              private router: Router,
              private docsSelect: DocumentselectService) {}  // Injecter Router ici

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  ngOnInit(): void {
    this.documentId= this.docsSelect.getSelectedDocument().id;
    console.log(this.documentId);
    this.loadDocument(this.documentId);
  }

  loadDocument(documentId: number): void {
    this.isLoading = true;  // Commencer le chargement
    this.documentService.getDocumentById(documentId).subscribe(
      (document: Document) => {
        if (document) {
          this.theme = document.theme || '';
          /* this.introduction = document.introduction || '';
          this.resumeDiaClasse = document.resumeDiaClasse || '';  
          this.resumeDiaSequence = document.resumeDiaSequence || '';  
          this.resumeDoc = document.resumeDoc || '';
          this.conclusion = document.conclusion || ''; */
        }
        this.isLoading = false;  // Terminer le chargement
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération du document : ' + (error.message || 'Erreur inconnue');
        this.isLoading = false;  // Terminer le chargement
      }
    );
  }

  verifyAndUpdateDocument() {
    // Créer un objet FormulaireVerification à partir des données du formulaire
    const formVerification: FormulaireVerification = {
      introduction: this.introduction,
      Problematique: this.Problematique,
      Etude_critique_existant: this.Etude_critique_existant,
      resumeDoc: this.resumeDoc,
      conclusion: this.conclusion
    };
  
    const fileToUpload = this.selectedFile ?? undefined;  // Remplace null par undefined
  
    this.isLoading = true;  // Indiquer que le processus de vérification commence
  
    // Appel au service pour vérifier et mettre à jour le document avec un fichier
    this.documentService.verifyAndUpdateDocumentWithFile(this.documentId, formVerification, fileToUpload).subscribe(
      (response: any) => {
        this.isLoading = false;  // Terminer le processus
  
        // Vérifier si la réponse est une chaîne de texte contenant un message de succès
        if (typeof response === 'string' && response.includes('Document mis à jour avec succès')) {
          this.showSuccessPopup(response);  // Afficher la popup de succès
        } else {
          // Gérer les autres cas comme un rejet ou une erreur
          this.showErrorPopup(response);
        }
      },
      (error) => {
        this.isLoading = false;  // Terminer le processus
        const errorMessage = error.error || 'Erreur inconnue';
        this.showErrorPopup(errorMessage);  // Afficher une popup pour les erreurs
      }
    );
  }

  // Méthode pour afficher un pop-up de succès avec le message du serveur
  private showSuccessPopup(successMessage: string): void {
    Swal.fire({
      icon: 'success',
      title: 'Succès',
      text: successMessage,  // Afficher le message de succès reçu du serveur
      confirmButtonText: 'OK'
    }).then(() => {
      // Redirection vers la page d'accueil après avoir cliqué sur "OK"
      this.router.navigate(['/app-chahib/home']);
    });
  }

  // Méthode pour afficher un pop-up pour les erreurs génériques
  private showErrorPopup(errorMessage: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Erreur',
      text: errorMessage,
      confirmButtonText: 'OK'
    });
  }

  
  // Méthode pour afficher une popup avec une réponse texte brute (comme "Formulaire rejeté...")
private showTextResponsePopup(textResponse: string): void {
  Swal.fire({
    icon: 'error',
    title: 'Réponse du serveur',
    text: textResponse,  // Afficher le texte brut renvoyé par le serveur ici
    confirmButtonText: 'OK'
  });
}


  // Méthode pour afficher un pop-up avec les détails de rejet
  private showRejectionPopup(response: any): void {
    let rejectionMessage = '<b>Le formulaire a été rejeté pour les raisons suivantes :</b><br><br>';

    if (response.introduction) {
      rejectionMessage += `<b>Introduction :</b> Similarité de ${response.introduction}<br>`;
    }
    if (response.Problematique) {
      rejectionMessage += `<b>Problématique :</b> Similarité de ${response.Problematique}<br>`;
    }
    if (response.Etude_critique_existant) {
      rejectionMessage += `<b>Étude Critique de l'Existant :</b> Similarité de ${response.Etude_critique_existant}<br>`;
    }
    if (response.resumeDoc) {
      rejectionMessage += `<b>Résumé Document :</b> Similarité de ${response.resumeDoc}<br>`;
    }

    Swal.fire({
      icon: 'error',
      title: 'Rejet du formulaire',
      html: rejectionMessage,
      confirmButtonText: 'OK'
    });
  }

  // Méthode pour afficher un pop-up pour les erreurs génériques
/*   private showErrorPopup(errorMessage: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Erreur',
      text: errorMessage,
      confirmButtonText: 'OK'
    });
  } */
}
