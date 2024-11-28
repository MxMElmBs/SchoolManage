import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-texbookhisto',
  standalone: true, // Activer les composants autonomes si utilisé dans Angular standalone
  imports: [CommonModule],
  templateUrl: './texbookhisto.component.html',
  styleUrls: ['./texbookhisto.component.css']
})
export class TexbookhistoComponent implements OnInit {
  dataSource2: any = {}; // Données regroupées par cours
  successMessage: string = '';
  selectedCourse: string | null = null; // Cours sélectionné
  courses: string[] = []; // Liste des noms des cours
  etudiantId= localStorage.getItem("etudiantId"); // ID de l'étudiant (à ajuster selon votre logique)
  apiUrlCahiers = 'http://localhost:8060/api/auth/etudiant/cahierparfiliere'; // API pour les cahiers de texte
  apiUrlSeances = 'http://localhost:8060/api/auth/etudiant/seance/open-without-cahier'; // API pour les séances sans cahier de texte
  seances: any[] = []; // Liste des séances sans cahier de texte

  // Nouveaux états
  loading: boolean = false; // Indicateur de chargement
  errorMessage: string | null = null; // Gestion des erreurs

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.getCahiersByEtudiantId();
    this.getOpenSeancesWithoutCahier();
  }

  // Fonction pour récupérer les cahiers de texte par filière pour un étudiant donné
  getCahiersByEtudiantId() {
    this.loading = true; // Activer l'indicateur de chargement
    this.errorMessage = null; // Réinitialiser le message d'erreur

    this.http.get<any[]>(`${this.apiUrlCahiers}/${this.etudiantId}`).subscribe(
      (data) => {
        this.dataSource2 = this.groupByCourse(data);
        this.courses = Object.keys(this.dataSource2);
        this.loading = false; // Désactiver l'indicateur de chargement
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération des cahiers de texte.';
        this.loading = false; // Désactiver l'indicateur de chargement même en cas d'erreur
        console.error('Erreur lors de la récupération des cahiers de texte', error);
      }
    );
  }

  // Fonction pour récupérer les séances sans cahier de texte pour un étudiant donné
  getOpenSeancesWithoutCahier() {
    this.loading = true; // Activer l'indicateur de chargement
    this.errorMessage = null; // Réinitialiser le message d'erreur

    this.http.get<any[]>(`${this.apiUrlSeances}/${this.etudiantId}`).subscribe(
      (data) => {
        this.seances = data;
        this.loading = false; // Désactiver l'indicateur de chargement
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération des séances sans cahier de texte.';
        this.loading = false; // Désactiver l'indicateur de chargement même en cas d'erreur
        console.error('Erreur lors de la récupération des séances sans cahier de texte', error);
      }
    );
  }

  // Fonction pour regrouper les cahiers de texte par nom du cours
  groupByCourse(data: any[], optionInitiale: string = 'Sélectionner un cours'): any {
    // Regrouper les cahiers par nom de cours
    const groupedData = data.reduce((acc, cahier) => {
      const nomCours = cahier.nomCours || 'Cours sans nom';
      if (!acc[nomCours]) {
        acc[nomCours] = [];
      }
      acc[nomCours].push(cahier);
      return acc;
    }, {});

    // Ajouter l'option initiale au début
    return {
      [optionInitiale]: null, // Option initiale
      ...groupedData
    };
  }


  // Fonction pour changer le cours sélectionné
  onSelectCourse(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.selectedCourse = target.value;
  }

  // Fonction pour rediriger vers la page d'ajout de cahier de texte
  navigateToAddCahier(seanceId: number) {
    this.router.navigate([`app-gigi/studentdash/textBook/${seanceId}`]);
  }
}
