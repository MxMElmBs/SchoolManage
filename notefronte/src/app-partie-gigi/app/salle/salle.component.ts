import { SeancePopupComponent } from './seance-popup.Component';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog'; // Importer MatDialog et MatDialogModule
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-salle',
  standalone: true, // Activer le mode standalone
  imports: [
    CommonModule,    // Import de CommonModule pour *ngIf, *ngFor, etc.
    FormsModule,     // Import de FormsModule pour les formulaires
    HttpClientModule, // Import de HttpClientModule pour les requêtes HTTP
    MatDialogModule  // Import de MatDialogModule pour les dialogues modaux
  ],
  templateUrl: './salle.component.html',
  styleUrls: ['./salle.component.css']
})
export class SalleComponent implements OnInit {
  seanceId: number | null = null;
  seanceDate: string | null = null;
  seanceStartTime: string | null = null;
  heuresRestantes: number | null = null;
  minutesRestantes: number | null = null;
  seanceFermee: boolean = false;
  ueId: number | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
    private dialog: MatDialog // Injection de MatDialog pour ouvrir des dialogues
  ) {}

  ngOnInit(): void {
    const ueInfo = localStorage.getItem('ueInfo');
    console.log("ueInfo:", ueInfo);
  
    if (ueInfo) {
      const parsedUeInfo = JSON.parse(ueInfo);
      this.ueId = parsedUeInfo.ueId;
    } else {
      console.error('Aucune information sur l\'UE trouvée.');
    }
  
    const seanceInfo = localStorage.getItem('seanceInfo');
    console.log("seanceInfo:", seanceInfo);
    
    if (seanceInfo) {
      const parsedSeanceInfo = JSON.parse(seanceInfo);
      this.seanceId = parsedSeanceInfo.seanceId;
      this.seanceDate = parsedSeanceInfo.date;
      this.seanceStartTime = parsedSeanceInfo.startTime;
    } else {
      this.seanceId = null;
    }
  
    // Ajoutez des console.log pour voir la valeur de seanceId et seanceFermee
    console.log('Seance ID:', this.seanceId);
    console.log('Seance Fermee:', this.seanceFermee);
  
    this.verifierSeance();
  }

  verifierSeance(): void {
    const seanceInfo = localStorage.getItem('seanceInfo');
    if (seanceInfo) {
      const parsedInfo = JSON.parse(seanceInfo);
      this.seanceId = parsedInfo.seanceId;
      this.seanceDate = parsedInfo.date;
      this.seanceStartTime = parsedInfo.startTime;
  
      this.http.get<{ fermee: boolean }>(`http://localhost:8060/api/auth/professeur/etat-seance/${this.seanceId}`)
        .subscribe(
          (response) => {
            console.log('Réponse de l\'API:', response);
            this.seanceFermee = response.fermee;
            if (!this.seanceFermee) {
              this.calculerTempsRestant();
            } else {
              this.reinitialiserSeance();
            }
          },
          (error) => {
            console.error('Erreur lors de la vérification de l\'état de la séance', error);
          }
        );
    }
  }

  ouvrirSalle(): void {
    if (this.seanceId) {
      this.openPopup();  // Ouvrir le popup si une séance est déjà ouverte
    } else {
      this.creerNouvelleSeance();
    }
  }

  openPopup(): void {
    const dialogRef = this.dialog.open(SeancePopupComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.fermerSeance(() => this.creerNouvelleSeance()); // Fermer la séance avant d'en ouvrir une nouvelle
      }
    });
  }

  creerNouvelleSeance(): void {
    const ueInfo = localStorage.getItem('ueInfo');
    if (ueInfo) {
      const parsedUeInfo = JSON.parse(ueInfo);

      this.http.post(`http://localhost:8060/api/auth/professeur/ouvrir-seance/${parsedUeInfo.ueId}`, {})
        .subscribe(
          (response: any) => {
            const seanceInfo = {
              seanceId: response.seanceId,
              date: response.date,
              startTime: response.startTime
            };
            localStorage.setItem('seanceInfo', JSON.stringify(seanceInfo));
            this.seanceId = seanceInfo.seanceId;
            this.seanceDate = seanceInfo.date;
            this.seanceStartTime = seanceInfo.startTime;
            this.seanceFermee = false;

            this.calculerTempsRestant();
          },
          error => {
            console.error('Erreur lors de l\'ouverture de la séance', error);
          }
        );
    } else {
      console.error('UE ID non trouvé');
    }
  }

  fermerSeance(callback?: Function): void {
    if (this.seanceId) {
      this.http.post(`http://localhost:8060/api/auth/professeur/fermer-seance/${this.seanceId}`, {})
      .subscribe(
        response => {
          console.log('Séance fermée avec succès', response);
          this.reinitialiserSeance();
          if (callback) {
            callback();
          }
          // Rechargez seulement les données nécessaires
          this.seanceFermee = true; // Ou tout autre état nécessaire
        },
        error => {
          console.error('Erreur lors de la fermeture de la séance', error);
        }
      );

    }
  }

  reinitialiserSeance(): void {
    localStorage.removeItem('seanceInfo');
    this.seanceId = null;
    this.seanceDate = null;
    this.seanceStartTime = null;
    this.heuresRestantes = null;
    this.minutesRestantes = null;
    this.seanceFermee = true;
  }

  calculerTempsRestant(): void {
    if (this.seanceStartTime && this.seanceDate) {
      const dateSeance = new Date(`${this.seanceDate}T${this.seanceStartTime}`);
      const maintenant = new Date();
      const differenceEnMillisecondes = 12 * 60 * 60 * 1000 - (maintenant.getTime() - dateSeance.getTime());

      if (differenceEnMillisecondes > 0) {
        this.heuresRestantes = Math.floor(differenceEnMillisecondes / (1000 * 60 * 60));
        this.minutesRestantes = Math.floor((differenceEnMillisecondes % (1000 * 60 * 60)) / (1000 * 60));
      } else {
        this.heuresRestantes = 0;
        this.minutesRestantes = 0;
      }
    }
  }
}
