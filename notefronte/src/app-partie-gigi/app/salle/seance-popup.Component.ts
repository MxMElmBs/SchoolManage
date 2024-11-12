import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-seance-popup',
  template: `
    <h2>Une séance est déjà ouverte</h2>
    <p>Souhaitez-vous fermer la séance actuelle avant d'en ouvrir une nouvelle ?</p>
    <div class="popup-actions">
      <button mat-button (click)="onConfirm()">Oui, fermer la séance</button>
      <button mat-button (click)="onCancel()">Annuler</button>
    </div>
  `,
  styles: [`
    .popup-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
    }
  `]
})
export class SeancePopupComponent {
  constructor(public dialogRef: MatDialogRef<SeancePopupComponent>) {}

  onConfirm(): void {
    this.dialogRef.close(true);  // Confirmer
  }

  onCancel(): void {
    this.dialogRef.close(false); // Annuler
  }
}
