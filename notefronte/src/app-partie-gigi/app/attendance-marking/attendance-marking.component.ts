import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-attendance-marking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './attendance-marking.component.html',
  styleUrls: ['./attendance-marking.component.css'],
})
export class AttendanceMarkingComponent implements OnInit {
  // Date sélectionnée pour le filtrage
  selectedDate: string = ''; // Date au format 'yyyy-MM-dd'

  // Données des séances récupérées depuis l'API
  seances: any[] = [];

  // Séances filtrées en fonction de la date sélectionnée
  filteredSeances: any[] = [];

  // seanceId pour vérifier si une séance est active
  seanceId: number | null = null;
  ueId: number | null = null;

  // Variable pour indiquer s'il y a des séances dans la base de données
  hasSeancesInDb: boolean = true;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    // Vérifier si les informations de séance sont dans localStorage
    const seanceInfo = localStorage.getItem('seanceInfo');
    if (seanceInfo) {
      this.seanceId = JSON.parse(seanceInfo).seanceId;
    }

    // Vérifier si les informations de l'UE sont dans localStorage
    const ueInfo = localStorage.getItem('ueInfo');
    if (ueInfo) {
      const parsedUeInfo = JSON.parse(ueInfo);
      this.ueId = parsedUeInfo.ueId;
      
      // Si l'ueId est valide, récupérer les séances
      if (this.ueId) {
        this.fetchSeances();
      } else {
        console.error('ueId est null ou invalide. Impossible de récupérer les séances.');
      }
    } else {
      console.error('Aucune information sur l\'UE trouvée.');
    }
  }

  // Récupérer les séances à partir de l'API
  fetchSeances(): void {
    if (!this.ueId) {
      console.error('ueId est null ou invalide. L\'appel API ne peut pas être effectué.');
      return;
    }
  
    this.http.get(`http://localhost:8060/api/auth/professeur/historique-presence/ue/${this.ueId}`)
      .subscribe(
        (response: any) => {
          this.seances = response;
  
          // Trier les séances par date du plus récent au plus ancien
          this.seances.sort((a: any, b: any) => new Date(b.seanceDate).getTime() - new Date(a.seanceDate).getTime());
  
          this.filteredSeances = this.seances; // Initialement, afficher toutes les séances
  
          // Met à jour la variable pour indiquer si des séances sont disponibles
          this.hasSeancesInDb = this.seances.length > 0;
        },
        (error) => {
          console.error('Erreur lors de la récupération des séances', error);
          this.hasSeancesInDb = false; // En cas d'erreur, suppose qu'il n'y a pas de séances
        }
      );
  }
  
  // Gestion du changement de date
  onDateChange(event: any): void {
    const selectedDate = event.target.value;
    this.selectedDate = selectedDate;

    // Filtrer les séances en fonction de la date sélectionnée
    if (selectedDate) {
      this.filteredSeances = this.seances.filter(seance => seance.seanceDate === selectedDate);
    } else {
      this.filteredSeances = this.seances;
    }
  }

  // Méthode pour naviguer vers la page de prise de présence
  goToPresence(): void {
    this.router.navigate(['/app-gigi/professeur/presence']);
  }
}
