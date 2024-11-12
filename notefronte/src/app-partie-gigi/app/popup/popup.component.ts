import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent {
  constructor(
    public dialogRef: MatDialogRef<PopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { ueId: number },
    private http: HttpClient,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  onYes(): void {
    // Vérifier si une séance est déjà ouverte pour cette UE
    this.http.get<{ seanceEnCours: boolean }>(`http://localhost:8060/api/auth/professeur/verifier-seance/${this.data.ueId}`)
      .subscribe(
        (response) => {
          if (response.seanceEnCours) {
            this.snackBar.open('Une séance est déjà ouverte pour cette UE.', 'Fermer', { duration: 3000 });
          } else {
            this.ouvrirNouvelleSeance();
          }
        },
        error => {
          console.error('Erreur lors de la vérification de la séance', error);
          this.snackBar.open('Erreur lors de la vérification de la séance', 'Fermer', { duration: 3000 });
          this.dialogRef.close();
        }
      );
  }

  ouvrirNouvelleSeance(): void {
    this.http.post(`http://localhost:8060/api/auth/professeur/ouvrir-seance/${this.data.ueId}`, {})
      .subscribe(
        (response: any) => {
          console.log('Nouvelle séance ouverte', response);
          const seanceInfo = {
            seanceId: response.seanceId,
            date: response.date,
            startTime: response.startTime
          };
          localStorage.setItem('seanceInfo', JSON.stringify(seanceInfo)); // Stocker la séance
          const ueInfo = { ueId: this.data.ueId };
          localStorage.setItem('ueInfo', JSON.stringify(ueInfo));

          this.snackBar.open('Séance ouverte avec succès', 'Fermer', { duration: 3000 });
          this.dialogRef.close();
          this.router.navigate(['/app-gigi/professeur/salle']); // Redirection après succès
        },
        error => {
          console.error('Erreur lors de l\'ouverture de la séance', error);
          this.snackBar.open('Erreur lors de l\'ouverture de la séance', 'Fermer', { duration: 3000 });
          this.dialogRef.close();
        }
      );
  }

  onNo(): void {
    localStorage.removeItem('seanceInfo'); // Supprimer les informations sur la séance en cours
    const ueInfo = { ueId: this.data.ueId };
    localStorage.setItem('ueInfo', JSON.stringify(ueInfo)); // Stocker uniquement l'UE sélectionnée
  
    // Message de confirmation et redirection
    this.snackBar.open('Redirection vers le dashboard sans ouvrir une nouvelle séance.', 'Fermer', { duration: 3000 });
    this.dialogRef.close();
    this.router.navigate(['/app-gigi/professeur/salle']); // Redirection vers la salle (dashboard)
  }
  

  onCancel(): void {
    this.snackBar.open('Action annulée.', 'Fermer', { duration: 2000 });
    this.dialogRef.close();
  }
}
