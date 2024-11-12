import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface cours {
  ueId: number;
  libelle: string;
  code: string;
  credit: number;
  descriptUe: string;
  typeUe: string;
  niveauEtude: string;
  typeSemestre: string;
  professeurNom: string;
  parcoursNom: string | null;
  filiereNom: string[]; // Tableau de filières
}

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  cours: cours | undefined;
  ueId: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.ueId = this.route.snapshot.paramMap.get('id'); // Récupération de l'ueId
    if (this.ueId) {
      this.getCoursById(this.ueId); // Utilisation de l'ueId
    }
  }

  getCoursById(id: string): void {
    const apiUrl = `http://localhost:8060/api/auth/de/uebyId/${this.ueId}`;
    this.http.get<cours>(apiUrl).subscribe(
      (response) => {
        this.cours = response;
      },
      (error) => {
        console.error('Erreur lors de la récupération des détails du cours', error);
      }
    );
  }

  goToHistoriqueCahierDeCharges(): void {
    if (this.ueId) {
      this.router.navigate([`/app-gigi/dashboard/resume/${this.ueId}`]); // Utiliser ueId pour la navigation
    }
  }  

  goToHistoriquePresence(): void {
    if (this.ueId) {
      this.router.navigate([`/app-gigi/dashboard/resumepresence/${this.ueId}`]);  // Navigue vers resumepresence avec ueId
    }
  }
}
