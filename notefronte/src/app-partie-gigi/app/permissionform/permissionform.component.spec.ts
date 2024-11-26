import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-permissionform',
  standalone: true,
  imports: [FormsModule, HttpClientModule],
  templateUrl: './permissionform.component.html',
  styleUrls: ['./permissionform.component.css']
})
export class PermissionformComponent {
  dateDebutAbsence: string | null = null;
  dateFinAbsence: string | null = null;
  raison: string = '';
  description: string = '';
  fichier: File | null = null;
  isLoading: boolean = false;

  constructor(private http: HttpClient, private router: Router) {}

  onFileChange(event: any) {
    this.fichier = event.target.files[0];
  }

  ajouterPermission() {
    const formData = new FormData();

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

    const etudiantId = localStorage.getItem("idetudiant");

    // Démarrer le chargement
    this.isLoading = true;

    this.http.post(`http://localhost:8060/api/auth/etudiant/add-permission/${etudiantId}`, formData, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.isLoading = false;
          Swal.fire('Succès', 'Permission ajoutée avec succès!', 'success');
          setTimeout(() => {
            this.router.navigate(['/app-gigi/studentdash/permhistorique']);
          }, 3000);
          this.resetForm();
        },
        error: (error) => {
          this.isLoading = false;
          Swal.fire('Erreur', 'Une erreur est survenue lors de l\'ajout de la permission.', 'error');
          console.error('Erreur:', error);
        }
      });
  }

  resetForm() {
    this.dateDebutAbsence = null;
    this.dateFinAbsence = null;
    this.raison = '';
    this.description = '';
    this.fichier = null;
  }
}
