import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import  { MatDialogModule, MatDialogRef } from '@angular/material/dialog';


@Component({
  selector: 'app-valider-popup',
  standalone: true,
  imports: [CommonModule,
    MatDialogModule,
    FormsModule
  ],
  templateUrl: './valider-popup.component.html',
  styleUrl: './valider-popup.component.css'
})
export class ValiderPopupComponent {
  constructor(
    public dialogRef: MatDialogRef<ValiderPopupComponent>
  ){}

  onClose(): void{
    this.dialogRef.close('close');
  }
  
}
