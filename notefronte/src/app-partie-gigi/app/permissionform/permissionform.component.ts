import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-permissionform',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './permissionform.component.html',
  styleUrls: ['./permissionform.component.css']
})
export class PermissionformComponent {
  dateDebutAbsence: string | null = null;
  dateFinAbsence: string | null = null;
  raison: string = '';
  description: string = '';
  fichier: File | null = null;
  isLoading: boolean = false; // Indique si une opération est en cours

  constructor(private http: HttpClient, private router: Router, private snackBar: MatSnackBar) {}

  onFileChange(event: any) {
    this.fichier = event.target.files[0];
  }

  ajouterPermission() {
    this.isLoading = true; // Démarre l'indicateur de chargement
    const formData = new FormData();

    // Ajout des données au formulaire
    if (this.dateDebutAbsence) {
      formData.append('dateDebutAbsence', this.dateDebutAbsence);
    }
    if (this.dateFinAbsence) {
      formData.append('dateFinAbsence', this.dateFinAbsence);
    }
    formData.append('raison', this.raison || '');
    formData.append('description', this.description || '');
    if (this.fichier) {
      formData.append('file', this.fichier);
    }

    // Récupération de l'identifiant étudiant
    const etudiantId = localStorage.getItem("idetudiant");

    // Envoi de la requête HTTP
    this.http.post(`http://localhost:8060/api/auth/etudiant/add-permission/${etudiantId}`, formData, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          // Affiche une notification de succès
          this.showPopup('Permission ajoutée avec succès!', 'success');
          // Redirection après un délai
          setTimeout(() => {
            this.router.navigate(['/app-gigi/studentdash/permhistorique']);
          }, 2000);
          // Réinitialisation du formulaire
          this.resetForm();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout de la permission', error);
          // Affiche une notification d'erreur
          const errorMessage = error.error || 'Une erreur est survenue. Veuillez réessayer.';
          this.showPopup(errorMessage, 'error');
        },
        complete: () => {
          // Arrête le chargement
          this.isLoading = false;
        }
      });
  }

  /**
   * Réinitialise les champs du formulaire
   */
  resetForm() {
    this.dateDebutAbsence = null;
    this.dateFinAbsence = null;
    this.raison = '';
    this.description = '';
    this.fichier = null;
  }

  /**
   * Affiche une popup de succès ou d'erreur
   * @param message Message à afficher
   * @param type Type de popup ('success' ou 'error')
   */
  private showPopup(message: string, type: 'success' | 'error') {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      panelClass: type === 'success' ? 'snackbar-success' : 'snackbar-error'
    });
  }
}

