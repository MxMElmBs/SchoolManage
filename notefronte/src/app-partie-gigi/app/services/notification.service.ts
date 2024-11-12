import { Injectable } from '@angular/core';  
import { MatSnackBar } from '@angular/material/snack-bar';  

@Injectable({  
  providedIn: 'root',  
})  
export class NotificationService {  
  constructor(private snackBar: MatSnackBar) {}  

  show(message: string, action: string = 'Fermer', duration: number = 3000) {  
    this.snackBar.open(message, action, {  
      duration: duration,  
      verticalPosition: 'top', // ou 'bottom'  
      horizontalPosition: 'right', // ou 'left' ou 'center'  
    });  
  }  
}