import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router'; // Import du RouterModule
import Swal from 'sweetalert2'; // Import de SweetAlert2

interface UEData {
  ueId: number;
  libelle: string;
  code: string;
  credit: number;
  professeurNom: string;
  tauxAchevement: number;
}

@Component({
  selector: 'app-matiere',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, RouterModule], // Ajout de RouterModule
  templateUrl: './matiere.component.html',
  styleUrls: ['./matiere.component.css']
})
export class MatiereComponent implements OnInit {
  dataSource2: UEData[] = [];
  filteredData: UEData[] = [];
  isLoading = false; // Ajouter un état de chargement

  filter = {
    nom: '',
    code: '',
    chargecours: ''
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getUEData();
  }

  getUEData(): void {
    const parcoursId = localStorage.getItem('parcoursId');
    const apiUrl = `http://localhost:8060/api/auth/de/ueParparcours/${parcoursId}`; // URL de l'API

    this.isLoading = true; // Activer l'état de chargement

    this.http.get<UEData[]>(apiUrl).subscribe((response) => {
      this.dataSource2 = response;
      console.log('Data course : ', this.dataSource2)
      this.filteredData = [...this.dataSource2]; // Copier les données pour filtrage
      this.isLoading = false; // Désactiver l'état de chargement
    }, (error) => {
      console.error('Erreur lors de la récupération des données de l\'API', error);
      this.isLoading = false; // Désactiver l'état de chargement en cas d'erreur
    });
  }

  // Méthode de filtrage
  onFilterChange(): void {
    this.filteredData = this.dataSource2.filter(item => {
      const matchesNom = item.libelle.toLowerCase().includes(this.filter.nom.toLowerCase());
      const matchesCode = item.code.toLowerCase().includes(this.filter.code.toLowerCase());
      const matchesChargeCours = item.professeurNom.toLowerCase().includes(this.filter.chargecours.toLowerCase());

      return matchesNom && matchesCode && matchesChargeCours;
    });
  }

  view() {
    console.log('View button clicked');
  }

  signaler(ueId: number): void {
    const apiUrl = `http://localhost:8060/api/auth/de/signaler/${ueId}`;

    this.isLoading = true; // Activer l'état de chargement

    // Afficher le popup de chargement
    Swal.fire({
      title: 'Chargement...',
      text: 'Veuillez patienter',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.http.put(apiUrl, {}).subscribe(
      response => {
        // Fermer le popup de chargement
        Swal.close();
        this.isLoading = false; // Désactiver l'état de chargement

        // Afficher le popup de succès
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'UE marked as retard successfully'
        });

        this.getUEData(); // Rafraîchir les données après marquage
      },
      error => {
        // Fermer le popup de chargement
        Swal.close();
        this.isLoading = false; // Désactiver l'état de chargement

        // Afficher le popup d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Erreur lors du marquage de l\'UE comme en retard'
        });

        console.error('Erreur lors du marquage de l\'UE comme en retard', error);
      }
    );
  }
}
