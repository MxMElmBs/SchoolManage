import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-poppresence',
  templateUrl: './poppresence.component.html',
  styleUrls: ['./poppresence.component.css']
})
export class PoppresenceComponent {
  constructor(public dialogRef: MatDialogRef<PoppresenceComponent>) {}

  onYesClose(): void {
    this.dialogRef.close('yes-close');
  }

  onYesNoClose(): void {
    this.dialogRef.close('yes-no-close');
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
