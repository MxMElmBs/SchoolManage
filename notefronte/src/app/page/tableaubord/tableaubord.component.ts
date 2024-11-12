import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UejeffService } from '../../services/UejeffService/uejeff.service';
import { EtudiantjeffService } from '../../services/EtudiantjeffService/etudiantjeff.service';
import { TableaudebordService } from '../../services/talbeaudebordService/tableaudebord.service';
import { Chart } from 'chart.js/auto';
import { EtudiantMoyenneDto } from '../../models/EtudiantMoyenneDto/etudiant-moyenne-dto';
import { CommonModule } from '@angular/common';
import { BulletinService } from '../../services/BulletinService/bulletin.service';
import { NiveauStats } from '../../models/NiveauStats/niveau-stats';

@Component({
  selector: 'app-tableaubord',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './tableaubord.component.html',
  styleUrl: './tableaubord.component.css'
})
export class TableaubordComponent implements OnInit {

  tauxReussiteUe: { [key: string]: number } = {};
  distributionNotes: { [key: string]: number } = {};
  totalUE: number = 0;
  uePremiereAnnee: number = 0;
  ueDeuxiemeAnnee: number = 0;
  ueTroisiemeAnnee: number = 0;
  totalEtudiants: number = 0;
  etudiantsPremiereAnnee: number = 0;
  etudiantsDeuxiemeAnnee: number = 0;
  etudiantsTroisiemeAnnee: number = 0;
  top5Etudiants: EtudiantMoyenneDto[] = [];
  statsParNiveau: NiveauStats[] = [];

  constructor(private ueService: UejeffService, private etudiantService: EtudiantjeffService,
    private dashboardService: TableaudebordService, private niveauService: BulletinService
  ) {}

  ngOnInit(): void {

    this.ueService.getNombreUE().subscribe(data => {
      this.totalUE = data.totalUE;
    });

    this.ueService.getUECountPremiereAnnee().subscribe(data => {
      this.uePremiereAnnee = data;
    });
    this.ueService.getUECountDeuxiemeAnnee().subscribe(data => {
      this.ueDeuxiemeAnnee = data;
    });
    this.ueService.getUECountTroisiemeAnnee().subscribe(data => {
      this.ueTroisiemeAnnee = data;
    });

    this.etudiantService.getNombreEtudiants().subscribe(data => {
      this.totalEtudiants = data.totalEtudiants;
      this.etudiantsPremiereAnnee = data.etudiantsPremiereAnnee;
      this.etudiantsDeuxiemeAnnee = data.etudiantsDeuxiemeAnnee;
      this.etudiantsTroisiemeAnnee = data.etudiantsTroisiemeAnnee;
    });

    this.loadTauxReussiteParUe();
    this.loadDistributionDesNotes();

    this.etudiantService.getTop5Etudiants().subscribe(data => {
      this.top5Etudiants = data;
    });

    this.niveauService.getStatsParNiveau().subscribe(stats => {
      this.statsParNiveau = stats;
    });

  }

  // Les méthodes

  loadTauxReussiteParUe(): void {
    this.dashboardService.getTauxReussiteParUe().subscribe(data => {
      this.tauxReussiteUe = data;
      this.createBarChart();
    });
  }

  loadDistributionDesNotes(): void {
    this.dashboardService.getDistributionDesNotes().subscribe(data => {
      this.distributionNotes = data;
      this.createPieChart();
    });
  }

  createBarChart(): void {
    new Chart("tauxReussiteChart", {
      type: 'bar',
      data: {
        labels: Object.keys(this.tauxReussiteUe),
        datasets: [{
          label: '% de Réussite',
          data: Object.values(this.tauxReussiteUe),
          backgroundColor: 'rgba(30, 144, 255, 0.6)',
        }]
      }
    });
  }

  createPieChart(): void {
    new Chart("distributionNotesChart", {
      type: 'pie',
      data: {
        labels: Object.keys(this.distributionNotes),
        datasets: [{
          data: Object.values(this.distributionNotes),
          backgroundColor: ['#ff6384', '#36a2eb', '#cc65fe', '#ffce56', '#33ff77']
        }]
      }
    });
  }



}
