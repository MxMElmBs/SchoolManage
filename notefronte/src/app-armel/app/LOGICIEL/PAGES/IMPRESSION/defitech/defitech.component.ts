import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Etudiant } from '../../../MODELS/etudiant';
import { DataSharingServiceService } from '../../../SERVICES/data-sharing-service.service';
import { ActivatedRoute } from '@angular/router';
import { FiliersService } from '../../../SERVICES/filiers.service';

@Component({
  selector: 'app-defitech',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './defitech.component.html',
  styleUrl: './defitech.component.css'
})
export class DefitechComponent  implements OnInit{
  currentYear: number = new Date().getFullYear(); // Année actuelle
  etudiants: Etudiant[] = [];
  selectedFiliere: string = ''; // Filtrage par filière
  niveauEtude: string = ''; // Niveau d'étude à récupérer via les paramètres

  constructor(
    private dataSharingService: DataSharingServiceService,
    private route: ActivatedRoute,
    private filiersService: FiliersService // Ajoutez le service pour récupérer les étudiants
  ) {}

  ngOnInit(): void {
    // Récupère les paramètres de requête pour la filière et le niveau
    this.route.queryParams.subscribe(params => {
      this.selectedFiliere = params['filiere'];
      this.niveauEtude = params['niveau'];
      this.loadStudents(); // Charge les étudiants après avoir récupéré les paramètres
    });

    // Abonnez-vous au service pour obtenir les étudiants
    this.dataSharingService.etudiants$.subscribe(etudiants => {
      this.etudiants = etudiants;
    });
  }

  loadStudents(): void {
    // Vérifiez si la filière et le niveau sont définis avant de charger
    if (this.selectedFiliere && this.niveauEtude) {
      this.filiersService.getEtudiantsByFiliereAndNiveau(this.selectedFiliere, this.niveauEtude).subscribe(
        (data: Etudiant[]) => {
          this.dataSharingService.updateEtudiants(data); // Met à jour les étudiants dans le service de partage
        },
        (error: any) => {
          console.error('Erreur lors de la récupération des étudiants', error);
        }
      );
    }
  }

  print(): void {
    window.print(); // Fonction d'impression
  }
}