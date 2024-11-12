import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field'; // Import du module MatFormField
import { MatInputModule } from '@angular/material/input'; // Import du module MatInput
import { CommonModule } from '@angular/common';  // Nécessaire pour les directives Angular de base

@Component({
  selector: 'app-permpopup',
  standalone: true,  // Composant autonome
  templateUrl: './permpopup.component.html',
  styleUrls: ['./permpopup.component.css'],
  imports: [
    CommonModule,  // Nécessaire pour les directives comme *ngIf et *ngFor
    FormsModule,   // Pour utiliser ngModel dans les formulaires
    MatDialogModule, // Pour MatDialog
    MatFormFieldModule, // Pour MatFormField
    MatInputModule // Pour les champs de texte avec MatInput
  ]
})
export class PermpopupComponent {
  inputText: string = ''; // Texte saisi dans le champ

  constructor(
    public dialogRef: MatDialogRef<PermpopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { type: string }
  ) {}

  ngOnInit(): void {
    // Fermer la popup après 30 secondes
    setTimeout(() => {
      this.dialogRef.close({ inputText: this.inputText });
    }, 60000);  // 30000 ms = 30 secondes
  }
  

  onSave(): void {
    this.dialogRef.close({ inputText: this.inputText });
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}
