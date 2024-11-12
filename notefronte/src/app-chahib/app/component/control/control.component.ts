import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document-service.service';
import { CreateDocumentDto } from '../../models/CreateDocumentDto';
import { Professeur } from '../../models/Professeur';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';  // Import SweetAlert2 for better popups
import { LocalStorageService } from '../../services/local-storage.service';

@Component({
  selector: 'app-control',
  templateUrl: './control.component.html',
  styleUrls: ['./control.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
  providers: [LocalStorageService]
})
export class ControlComponent implements OnInit {
  documentType: string = '';
  theme: string = '';
  etudiantId: number = 0;  // Initialisation ici
  selectedProfesseur: Professeur = new Professeur();
  newDocument: CreateDocumentDto = {} as CreateDocumentDto;  // Correct initialization
  isThemeValid: boolean = true;
  showMessage: boolean = false;
  professeurs: Professeur[] = [];
  isLoading: boolean = true;

  constructor(
    private documentService: DocumentService,
    private route: Router,
    private studentSave: LocalStorageService

  ) {}

  ngOnInit(): void {
    // Récupérer l'ID étudiant depuis le localStorage
    this.etudiantId = this.studentSave.getStudentId();

    if (this.etudiantId === 0) {
      Swal.fire('Erreur', 'ID étudiant non trouvé. Veuillez réessayer.', 'error');
    } else {
      this.getProfesseurs();  // Charger la liste des professeurs si l'ID étudiant est valide
    }
  }

  getProfesseurs() {
    this.isLoading = true;  // Afficher le spinner de chargement
    this.documentService.getAllProfesseurs().subscribe(
      (data) => {
        this.professeurs = data;
        this.isLoading = false;  // Masquer le spinner de chargement après la récupération
      },
      (error) => {
        this.isLoading = false;  // Masquer le spinner en cas d'erreur
        Swal.fire('Erreur', 'Erreur lors de la récupération des professeurs', 'error');
      }
    );
  }

  handleSubmit() {
    // Vérification des champs obligatoires
    if (!this.documentType || !this.theme || !this.etudiantId || !this.selectedProfesseur.professeurId) {
      Swal.fire('Erreur', 'Tous les champs sont obligatoires.', 'error');
      return;
    }

    this.isLoading = true;  // Afficher le spinner de chargement avant de vérifier l'existence du document

    // Vérification si le document existe déjà
    this.documentService.checkDocumentExists(this.etudiantId, this.documentType).subscribe(
      (response: any) => {
        if (response.message.includes('existe déjà')) {
          this.isLoading = false;  // Masquer le spinner si le document existe
          Swal.fire('Attention', response.message, 'warning');  // Avertissement si le document existe
        } else {
          // Créer un nouveau document
          this.newDocument = {
            typeDocument: this.documentType,
            theme: this.theme,
            etudiantId: this.etudiantId,
            professeurId: this.selectedProfesseur.professeurId
          };

          this.documentService.addDocument(this.newDocument).subscribe(
            (response) => {
              this.isLoading = false;  // Masquer le spinner après la création
              this.handleReset();  // Réinitialiser le formulaire
              this.showConfirmationPopup();  // Afficher la confirmation
            },
            (error) => {
              this.isLoading = false;  // Masquer le spinner en cas d'erreur lors de la soumission
              Swal.fire('Erreur', 'Thème rejeté. Veuillez choisir un autre thème.', 'error');
            }
          );
        }
      },
      (error) => {
        this.isLoading = false;  // Masquer le spinner si la vérification échoue
        Swal.fire('Erreur', `Thème rejeté : ${error.error.message}`, 'error');
      }
    );
  }

  showConfirmationPopup(): void {
    Swal.fire({
      icon: 'success',
      title: 'Document créé avec succès',
      text: 'Le document a été créé avec succès.',
      confirmButtonText: 'Ok',
    }).then((result) => {
      if (result.isConfirmed) {
        this.route.navigate(['/app-chahib/home']);  // Redirection après confirmation
      }
    });
  }

  handleReset() {
    // Réinitialisation du formulaire
    this.documentType = '';
    this.theme = '';
    this.etudiantId = 0;
    this.selectedProfesseur = new Professeur();
    this.isThemeValid = true;
  }

  logout() {
    // Ajoutez ici la logique de déconnexion (par exemple, supprimer les tokens, rediriger vers la page de connexion, etc.)
    this.route.navigate(['/']);
  }
}
