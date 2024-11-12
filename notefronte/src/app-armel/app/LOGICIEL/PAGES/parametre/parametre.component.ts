import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { Paiement } from '../../MODELS/paiement';
import { EcheanceService } from '../../SERVICES/echeance.service';

@Component({
  selector: 'app-parametre',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatIconModule],
  templateUrl: './parametre.component.html',
  styleUrl: './parametre.component.css'
})
export class ParametreComponent {
  user = {
    name: 'Jean Dupont',
    email: 'jean.dupont@example.com',
    phone: '+33 6 12 34 56 78',
    address: '123 Rue du Faubourg, Paris, France',
    avatarUrl: 'https://th.bing.com/th/id/R.660b56fcb220dc2a08aab4b568f2f399?rik=9T2NiMsumr6wNA&pid=ImgRaw&r=0'
  };

  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  constructor(private echeanceService: EcheanceService) { }

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
