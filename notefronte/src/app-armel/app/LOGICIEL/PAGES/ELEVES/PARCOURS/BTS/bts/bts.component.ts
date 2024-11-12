import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Statistiques } from '../../../../../MODELS/statistiques';
import { ParcourService } from '../../../../../SERVICES/parcour.service';
import { Paiement } from '../../../../../MODELS/paiement';
import { EcheanceService } from '../../../../../SERVICES/echeance.service';

@Component({
  selector: 'app-bts',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './bts.component.html',
  styleUrl: './bts.component.css'
})
export class BtsComponent {
  statistiques: Statistiques[] = [];
  parcoursId: Number = 2;  // Par exemple
  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  constructor(    private echeanceService: EcheanceService,
    private statistiquesService: ParcourService) { }

  ngOnInit(): void {
    this.getStatistiques();

    this.echeanceService.getRappelsAVenir().subscribe(
      (data: Paiement[]) => {
        this.rappels = data;
        this.loading = false;
      },
      (err) => {
        this.error = 'Erreur lors du chargement des rappels.';
        this.loading = false;
      }
    );
  

  this.echeanceService.getNombreRappelsAVenir().subscribe(
    (nombre: number) => {
      this.nombreRappels = nombre; // Stocker le nombre dans la variable
    },
    (err) => {
      console.error('Erreur lors de la récupération du nombre de rappels.', err);
    }
  );
  }

  // Méthode pour récupérer les statistiques des étudiants
  getStatistiques(): void {
    this.statistiquesService.getStatistiquesParParcours(this.parcoursId)
      .subscribe((data: Statistiques[]) => {
        this.statistiques = data;
      }, error => {
        console.error('Erreur lors de la récupération des statistiques', error);
      });
  }
  


}
