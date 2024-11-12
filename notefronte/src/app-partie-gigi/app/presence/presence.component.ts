import { PoppresenceComponent } from './../poppresence/poppresence.component';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog'; // Importer MatDialog
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-presence',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './presence.component.html',
  styleUrls: ['./presence.component.css']
})
export class PresenceComponent implements OnInit {
  date: string = '';
  nomCours: string = '';
  etudiants: any[] = [];
  seanceId: number | null = null;

  constructor(private http: HttpClient, private router: Router, private dialog: MatDialog, private snackBar: MatSnackBar) {} // Injecter MatDialog

  ngOnInit(): void {
    this.seanceId = this.getSeanceIdFromLocalStorage();
    if (this.seanceId) {
      this.getEtudiants(this.seanceId);
    }
  }

  getSeanceIdFromLocalStorage(): number | null {
    const seanceInfo = localStorage.getItem('seanceInfo');
    return seanceInfo ? JSON.parse(seanceInfo).seanceId : null;
  }

  getEtudiants(seanceId: number): void {
    this.http.get<any[]>(`http://localhost:8060/api/auth/professeur/presences-seance/${seanceId}`)
      .subscribe(data => {
        this.etudiants = data;
      });
  }

  openPresenceDialog(): void {
    const dialogRef = this.dialog.open(PoppresenceComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'yes-close') {
        this.submitPresences(true);
      } else if (result === 'yes-no-close') {
        this.submitPresences(false);
      }
    });
  }

  submitPresences(closeSession: boolean): void {
    if (this.seanceId) {
      // Créer la liste des données de présence
      const presenceData = this.etudiants.map(etudiant => {
        if (!etudiant.etudiantId) {
          console.error('L\'ID de l\'étudiant est manquant pour', etudiant);
          return null;
        }
  
        return {
          etudiantId: etudiant.etudiantId,
          nomEtudiant: etudiant.nom,
          prenomEtudiant: etudiant.prenom,
          matriculeEtudiant: etudiant.matricule,
          present: etudiant.present,
          enPermission: etudiant.enPermission
        };
      }).filter(presence => presence !== null); // Filtrer les entrées nulles
  
      // Vérifier s'il y a des données valides à envoyer
      if (presenceData.length === 0) {
        console.error('Aucune donnée de présence valide à envoyer.');
        return;
      }
  
      // Définir l'URL de l'API en fonction de l'option de fermeture de la session
      const apiUrl = closeSession
        ? `http://localhost:8060/api/auth/professeur/prendrepresencesCloreSeance/${this.seanceId}`
        : `http://localhost:8060/api/auth/professeur/prendrepresences/${this.seanceId}`;
  
      // Envoyer la requête POST avec la liste des présences
      this.http.post(apiUrl, presenceData).subscribe(
        response => {
          // Afficher un message de succès
          this.snackBar.open('Présences soumises avec succès!', 'Fermer', {
            duration: 3000, // Le message sera visible pendant 3 secondes
          });
  
          // Rediriger après un délai de 3 secondes
          setTimeout(() => {
            this.router.navigate(['/app-gigi/professeur/attendance']);
          }, 3000); // Délai de 3 secondes avant la redirection
        },
        error => {
          console.error('Erreur lors de la soumission des données de présence:', error);
          this.snackBar.open('Échec de la soumission des présences.', 'Fermer', {
            duration: 3000,
          });
        }
      );
    }
  }
   
}
