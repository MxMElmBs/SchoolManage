import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';  // Importer HttpClient pour les appels API
import { ChangeDetectorRef } from '@angular/core'; // Importer ChangeDetectorRef pour détecter les changements
import { Router } from '@angular/router'; // Importer Router pour rediriger vers la page des détails
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-permission',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, NgOptimizedImage],
  templateUrl: './permission.component.html',
  styleUrls: ['./permission.component.css']
})
export class PermissionComponent {
  dataSource2: any[] = [];  // Variable pour stocker les permissions
  isLoading = false;  // Pour indiquer si les données sont en cours de chargement
  sortAscending = true;  // Gérer le tri (croissant ou décroissant)

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef, private router: Router) {}

  // Méthode appelée au chargement du composant
  ngOnInit(): void {
    this.fetchPermissions();
  }

  // Méthode pour récupérer les permissions via l'API
  fetchPermissions() {
    this.isLoading = true;

    const parcoursId = localStorage.getItem('parcoursId');
    console.log('ID du parcours:', parcoursId);
    const apiUrl = `http://localhost:8060/api/auth/de/permission-par-parcours/${parcoursId}`;

    this.http.get<any[]>(apiUrl).subscribe(
      (response) => {
        this.dataSource2 = response;
        console.log('Données chargées:', this.dataSource2);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      (error) => {
        console.error('Erreur lors du chargement des permissions:', error);
        this.isLoading = false;
      }
    );
  }

  view(permissionId: number) {
    this.router.navigate(['/app-gigi/dashboard/detailperm', permissionId]);
  }

  // Méthode pour trier les données par date de demande
  sortByDateDemande() {
    this.sortAscending = !this.sortAscending;  // Inverser l'ordre de tri
    this.dataSource2.sort((a, b) => {
      const dateA = new Date(a.dateDemande).getTime();
      const dateB = new Date(b.dateDemande).getTime();
      return this.sortAscending ? dateA - dateB : dateB - dateA;
    });
    this.cdr.detectChanges();
  }
}
