import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DurationPipe } from './duration.pipe';

@Component({
  selector: 'app-emploidutemps',
  standalone: true,
  imports: [CommonModule, DurationPipe],  
  templateUrl: './emploidutemps.component.html',
  styleUrls: ['./emploidutemps.component.css']
})
export class EmploidutempsComponent implements OnInit {

  cahierTextes: any[] = [];
  ueId: number | null = null;
  seanceId: number | null = null;
  nomCours: string | null = null; // Nouveau champ pour stocker le nom du cours
  apiUrl = 'http://localhost:8060/api/auth/professeur/cahierParUe/';
  checkCahierUrl = 'http://localhost:8060/api/auth/professeur/checkEnregistrer/';
  showButton = false;  

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.retrieveLocalStorageValues();

    if (this.seanceId) {
      this.checkIfCahierEnregistrer();
    }

    if (this.ueId) {
      this.getCahiersDeTexteParUe();
    }
  }

  retrieveLocalStorageValues(): void {
    const ueInfo = localStorage.getItem('ueInfo');
    const seanceInfo = localStorage.getItem('seanceInfo');
    
    if (ueInfo) {
      const parsedUeInfo = JSON.parse(ueInfo);
      this.ueId = parsedUeInfo.ueId;
    } else {
      console.error('Aucune information sur l\'UE trouvée.');
    }

    if (seanceInfo) {
      const parsedSeanceInfo = JSON.parse(seanceInfo);
      this.seanceId = parsedSeanceInfo.seanceId;
    } else {
      console.error('Aucune information sur la séance trouvée.');
    }
  }

  checkIfCahierEnregistrer() {
    if (this.seanceId) {
      this.http.get<boolean>(`${this.checkCahierUrl}${this.seanceId}`).subscribe((response) => {
        this.showButton = response;
      }, error => {
        console.error('Erreur lors de la vérification du statut ENREGISTRER', error);
      });
    }
  }

  getCahiersDeTexteParUe() {
    if (this.ueId) {
      this.http.get<any[]>(`${this.apiUrl}${this.ueId}`).subscribe((data) => {
        this.cahierTextes = data;

        // Si la liste n'est pas vide, récupérer le nom du cours à partir du premier élément
        if (data.length > 0) {
          this.nomCours = data[0].nomCours;
        }
      }, error => {
        console.error('Erreur lors de la récupération des cahiers de texte', error);
      });
    }
  }

  navigateToPage() {
    if (this.seanceId) {
      this.router.navigate([`/app-gigi/professeur/textBook/${this.seanceId}`]); 
    } else {
      console.error('Impossible de naviguer, seanceId est manquant.');
    }
  }

}
