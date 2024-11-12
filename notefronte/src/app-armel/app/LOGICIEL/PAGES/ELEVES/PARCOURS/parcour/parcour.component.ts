import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ParcourService } from '../../../../SERVICES/parcour.service';
import { CommonModule } from '@angular/common';
import { Paiement } from '../../../../MODELS/paiement';
import { EcheanceService } from '../../../../SERVICES/echeance.service';

@Component({
  selector: 'app-parcour',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './parcour.component.html',
  styleUrl: './parcour.component.css'
})
export class ParcourComponent implements OnInit {
  stats: any[] = [];
  rappels: Paiement[] = [];
loading = true;
error: string | null = null;
nombreRappels: number = 0; 
  constructor(private parcourService: ParcourService,
    private echeanceService: EcheanceService
  ) {}

  ngOnInit(): void {
    this.loadStats();
    
this.echeanceService.getNombreRappelsAVenir().subscribe(
  (nombre: number) => {
    this.nombreRappels = nombre; // Stocker le nombre dans la variable
  },
  (err) => {
    console.error('Erreur lors de la récupération du nombre de rappels.', err);
  }
);
  }

  loadStats(): void {
    this.parcourService.getStats().subscribe(data => {
      this.stats = data;
    });
  }
}




