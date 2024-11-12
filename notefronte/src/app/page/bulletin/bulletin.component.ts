import { AfterViewInit, Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import $ from 'jquery';
import 'datatables.net';
import { Etudiantjeff } from '../../models/etudiantjeff/etudiantjeff';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { EtudiantjeffService } from '../../services/EtudiantjeffService/etudiantjeff.service';
import { CommonModule } from '@angular/common';
import { NiveauEtude } from '../../models/enums/niveau-etude';
import { EtudiantDto } from '../../models/EtudiantDto/etudiant-dto';
import { BulletinService } from '../../services/BulletinService/bulletin.service';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import { createPopper } from '@popperjs/core';
import bootstrap from 'bootstrap';

@Component({
  selector: 'app-bulletin',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule, ReactiveFormsModule, MatProgressBarModule, MatButtonModule, MatMenuModule],
  templateUrl: './bulletin.component.html',
  styleUrl: './bulletin.component.css'
})
export class BulletinComponent implements OnInit {

  niveaux = Object.keys(NiveauEtude);
  etudiants: Etudiantjeff[] = [];
  etudiant: EtudiantDto[] = [];
  selectedNiveau!: NiveauEtude;
  selectedEtudiant!: EtudiantDto | null;
  etudiantDetails!: EtudiantDto | null;
  selectedEtudiantId!: number | null;
  ueDetailsBySemestre: { [key: string]: any } = {};
  moyenneNiveau: number = 0;
  moyennesSemestres: { [key: string]: number } = {};
  mention: string = '';
  isLoading: boolean = false;
  messageEnvoye: string = '';
  messageErreur: string = '';
  timeoutDuration: number = 5000;

  niveauForm!: FormGroup;

  constructor(
    private etudiantService: EtudiantjeffService,
    private bulletinService: BulletinService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {

     // Initialiser le formulaire réactif

  }

  // Récupérer les étudiants en fonction du niveau sélectionné
  onNiveauChange() {
    if (this.selectedNiveau) {
      this.etudiantService.getEtudiantsByNiveau(this.selectedNiveau).subscribe(data => {
        this.etudiants = data;
        this.selectedEtudiantId = null; // Réinitialiser l'étudiant si le niveau change
        this.etudiantDetails = null;
      });
    }
  }

  onEtudiantSelect(event: Event) {
    const target = event.target as HTMLSelectElement;
    const id = Number(target.value); // Récupérer l'ID de l'étudiant sélectionné
    this.selectedEtudiantId = id;
  }
  // Vérifier si le bouton doit être activé ou désactivé
  isProceedDisabled(): boolean {
    return !(this.selectedNiveau && this.selectedEtudiantId);
  }

  getSemestres(): string {
    switch (this.selectedNiveau) {
      case NiveauEtude.PREMIERE_ANNEE:
        return 'Semestre 1 - Semestre 2';
      case NiveauEtude.DEUXIEME_ANNEE:
        return 'Semestre 3 - Semestre 4';
      case NiveauEtude.TROISIEME_ANNEE:
        return 'Semestre 5 - Semestre 6';
      default:
        return '';
    }
  }

  // Afficher les informations de l'étudiant sélectionné
  onAfficherEtudiant() {
    if (this.selectedEtudiantId) {
      this.isLoading = true;
      this.etudiantService.getEtudiantById(this.selectedEtudiantId).subscribe(details => {
        this.etudiantDetails = details; // Stocker les détails de l'étudiant
      });
      this.bulletinService.getUeDetailsParSemestre(this.selectedEtudiantId).subscribe(data => {
        this.ueDetailsBySemestre = data.ueDetailsBySemestre;
        this.moyenneNiveau = data.moyenneNiveau;
        this.mention = data.mention;
        // Extraire les moyennes des semestres
        Object.keys(data).forEach((key) => {
          if (key.startsWith('moyenne')) {
            this.moyennesSemestres[key] = data[key];
          }
        });
        this.isLoading = false;
      }, error => {
        console.error('Erreur lors de la récupération des détails:', error);
        this.isLoading = false;
      });

    }
  }

  // Méthode pour télécharger le bulletin PDF
  telechargerBulletinPDF() {
    if (this.selectedEtudiantId) {
      this.isLoading = true;
      this.bulletinService.genererBulletinPDF(this.selectedEtudiantId).subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `bulletin_${this.selectedEtudiantId}.pdf`;
        a.click();
        this.isLoading = false;
      }, error => {
        console.error('Erreur lors du téléchargement du PDF', error);
        this.isLoading = false;
      });
    }
  }

  // Méthode pour télécharger le bulletin Excel
  telechargerBulletinExcel() {
    if (this.selectedEtudiantId) {
      this.isLoading = true;
      this.bulletinService.genererBulletinExcel(this.selectedEtudiantId).subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `bulletin_${this.selectedEtudiantId}.xlsx`;
        a.click();
        this.isLoading = false;
      }, error => {
        console.error('Erreur lors du téléchargement de l\'Excel', error);
        this.isLoading = false;
      });
    }
  }

   // Méthode pour envoyer le bulletin par e-mail
   envoyerBulletinParEmail() {
    if (this.selectedEtudiantId) {
      this.isLoading = true;
      this.bulletinService.envoyerBulletinParEmail(this.selectedEtudiantId).subscribe(response => {
        this.messageEnvoye = response;  // Stocker le message de succès
        this.clearMessagesAfterTimeout();

        this.isLoading = false;
      }, error => {
        console.error('Erreur lors de l\'envoi du bulletin', error);
        this.messageErreur = 'Erreur lors de l\'envoi du bulletin.';  // Stocker le message d'erreur
        this.clearMessagesAfterTimeout();
        this.isLoading = false;
      });
    }
  }

  clearMessagesAfterTimeout() {
    setTimeout(() => {
      this.messageEnvoye = '';
      this.messageErreur = '';
    }, this.timeoutDuration);
  }

  getSemestreName(semestre: string): string {
    const mapping = {
      'SEMESTRE_1': 'Semestre 1',
      'SEMESTRE_2': 'Semestre 2',
      'SEMESTRE_3': 'Semestre 3',
      'SEMESTRE_4': 'Semestre 4',
      'SEMESTRE_5': 'Semestre 5',
      'SEMESTRE_6': 'Semestre 6'
    };
    return mapping[semestre as keyof typeof mapping] || semestre;
  }



}
