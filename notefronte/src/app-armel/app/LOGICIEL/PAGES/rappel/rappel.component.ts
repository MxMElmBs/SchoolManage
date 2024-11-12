import { Component, OnInit } from '@angular/core';
import { EcheanceService } from '../../SERVICES/echeance.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Paiement } from '../../MODELS/paiement';

@Component({
  selector: 'app-rappel',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './rappel.component.html',
  styleUrl: './rappel.component.css'
})
export class RappelComponent implements OnInit {

  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  constructor(
    private echeanceService: EcheanceService
  ) { }

  ngOnInit(): void {
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
 

 
}