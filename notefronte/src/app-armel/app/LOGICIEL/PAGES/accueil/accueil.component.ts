import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { EtudiantService } from '../../SERVICES/etudiant.service';
import { Paiement } from '../../MODELS/paiement';
import { EcheanceService } from '../../SERVICES/echeance.service';

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [  RouterLink, MatCardModule, MatRadioModule, MatIconModule],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent  {
  count: number | undefined;
  nonjour : number | undefined;
  ajour : number | undefined;
  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  constructor( private etudiantService: EtudiantService,
    private echeanceService: EcheanceService
  ) { }

  
    ngOnInit(): void {
      this.etudiantService.getCount().subscribe(
        (data: number) => {
          this.count = data;
        },
        (error) => {
          console.error('Error fetching count', error);
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
        this.etudiantService.getPartiAttente().subscribe(
        (data: number) => {
          this.nonjour = data;
        },
        (error) => {
          console.error('Error fetching count', error);
        }
      );

      this.etudiantService.getTpayee().subscribe(
        (data: number) => {
          this.ajour = data;
        },
        (error) => {
          console.error('Error fetching count', error);
        }
      );
    }
}
