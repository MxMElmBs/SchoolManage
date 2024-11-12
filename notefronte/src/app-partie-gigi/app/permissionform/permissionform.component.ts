import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private http: HttpClient, private router: Router, private snackBar: MatSnackBar) {}

  onFileChange(event: any) {
    this.fichier = event.target.files[0];
  }

  ajouterPermission() {
    const formData = new FormData();

    if (this.dateDebutAbsence) {
      formData.append('dateDebutAbsence', this.dateDebutAbsence);
    } else {
      console.error('dateDebutAbsence is null');
    }

    if (this.dateFinAbsence) {
      formData.append('dateFinAbsence', this.dateFinAbsence);
    } else {
      console.error('dateFinAbsence is null');
    }

    formData.append('raison', this.raison || '');
    formData.append('description', this.description || '');

    if (this.fichier) {
      formData.append('file', this.fichier);
    }

    const etudiantId = localStorage.getItem("idetudiant");
    this.http.post(`http://localhost:8060/api/auth/etudiant/add-permission/${etudiantId}`, formData, { responseType: 'text' })
      .subscribe(response => {
        console.log('Permission ajoutée avec succès', response);
        this.snackBar.open('Permission ajoutée avec succès!', 'Fermer', { duration: 2000 });
        setTimeout(() => {
          this.router.navigate(['/app-gigi/studentdash/permhistorique']);
        }, 4000);
        this.resetForm();
      }, error => {
        console.error('Erreur lors de l\'ajout de la permission', error);
        const errorMessage = error.error || 'Une erreur est survenue';
        this.snackBar.open(errorMessage, 'Fermer', { duration: 3000 });
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
