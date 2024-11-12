import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog'; // Import MAT_DIALOG_DATA
import { HttpClient } from '@angular/common/http'; // Import pour les appels API
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-participants',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './participants.component.html',
  styleUrls: ['./participants.component.css'],
  providers: [{ provide: MAT_DIALOG_DATA, useValue: {} }] // Ajoutez ceci si vous utilisez MatDialog
})
export class ParticipantsComponent implements OnInit {
  participants: { id: number, name: string, firstname: string, presence: boolean }[] = [];
  ueId: number; // Stocker l'ueId
  students: any[] = []; // Liste des étudiants

  constructor(
    public dialog: MatDialog,
    private http: HttpClient, 
    @Inject(MAT_DIALOG_DATA) public data: { ueId: number } // Injecter l'ueId si nécessaire
  ) {
    this.ueId = 0; // Initialiser l'ueId avec une valeur par défaut
  }

  ngOnInit() {
    // Récupérer l'UE ID depuis localStorage
    const storedUeId = localStorage.getItem('selectedUeId');
    if (storedUeId) {
      this.ueId = parseInt(storedUeId, 10); // Convertir l'UE ID en nombre
      console.log('ueId reçu dans ParticipantsComponent:', this.ueId);
      this.fetchStudents(); // Assurez-vous que l'ueId est bien défini avant l'appel
    } else {
      console.error('Aucune UE sélectionnée');
    }
  }
  
  fetchStudents() {
    if (this.ueId) {
      this.http.get<any[]>(`http://localhost:8060/api/auth/professeur/etudiantbyUE/${this.ueId}`)
        .subscribe(
          (students: any[]) => {
            this.students = students;
          },
          (error) => {
            console.error('Erreur lors de la récupération des étudiants:', error);
          }
        );
    } else {
      console.error('ueId est undefined ou null lors de l\'appel de fetchStudents');
    }
  }

  // Autres méthodes (submit, togglePresence, etc.)
}
