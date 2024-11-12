import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { ActivatedRoute } from '@angular/router';

interface CahierDto {
  date: string;
  heureDebut: string;
  heureFin: string;
  contenu: string;
  nomCours: string;
  credit: number;  // Ajout de la propriété "credit" à l'interface
}

@Component({
  selector: 'app-resumecahier',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatIconModule, MatGridListModule],
  templateUrl: './resumecahier.component.html',
  styleUrls: ['./resumecahier.component.css']
})
export class ResumecahierComponent implements OnInit {
  historiqueSeances: CahierDto[] = [];
  displayedColumns: string[] = ['date', 'heureDebut', 'heureFin', 'duree', 'contenu'];
  ueId: string | null = null;
  totalHeuresPrevues: number = 0; // Durée totale prévue du cours en heures

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.ueId = this.route.snapshot.paramMap.get('ueId');
    if (this.ueId) {
      this.getCahierByUeId(this.ueId);
    }
  }

  getCahierByUeId(ueId: string): void {
    const apiUrl = `http://localhost:8060/api/auth/de/cahierParUe/${ueId}`;
    this.http.get<CahierDto[]>(apiUrl).subscribe(
      (data) => {
        this.historiqueSeances = data;

        if (data.length > 0) {
          const credit = data[0].credit; // Supposons que le crédit est le même pour toutes les séances
          this.totalHeuresPrevues = credit * 12; // Chaque crédit correspond à 12 heures
        }
      },
      (error) => {
        console.error('Erreur lors de la récupération des cahiers de texte', error);
      }
    );
  }

  // Calcul du nombre total de séances
  getTotalSeances(): number {
    return this.historiqueSeances.length;
  }

  // Calcul du nombre total d'heures effectuées
  getTotalHours(): number {
    let totalMinutes = 0;
    this.historiqueSeances.forEach(seance => {
      totalMinutes += this.getDureeEnMinutes(seance.heureDebut, seance.heureFin);
    });
    const totalHours = totalMinutes / 60;
    return parseFloat(totalHours.toFixed(2)); // Formater le résultat avec deux chiffres après la virgule
  }
  

  // Calcul du pourcentage d'achèvement du cours
  // Calcul du pourcentage d'achèvement du cours
  getCompletionPercentage(): number {
    const totalHeures = this.getTotalHours();
    const completionPercentage = this.totalHeuresPrevues > 0 ? Math.min((totalHeures / this.totalHeuresPrevues) * 100, 100) : 0;
    return parseFloat(completionPercentage.toFixed(2)); // Formater le pourcentage à deux chiffres après la virgule
  }
  // Calcul de la durée entre heure de début et heure de fin
  getDuree(heureDebut: string, heureFin: string): string {
    const dureeMinutes = this.getDureeEnMinutes(heureDebut, heureFin);
    const heures = Math.floor(dureeMinutes / 60);
    const minutes = dureeMinutes % 60;
    return `${heures}h ${minutes}min`;
  }

  // Calcul de la durée en minutes
  getDureeEnMinutes(heureDebut: string, heureFin: string): number {
    const [heureDebutHeures, heureDebutMinutes] = heureDebut.split(':').map(Number);
    const [heureFinHeures, heureFinMinutes] = heureFin.split(':').map(Number);
    const debut = heureDebutHeures * 60 + heureDebutMinutes;
    const fin = heureFinHeures * 60 + heureFinMinutes;
    return fin - debut;
  }
  notifierProfesseur(cours: string) {  
    alert(`Le professeur a été notifié que le cours ${cours} est en retard.`);  
  } 
}
