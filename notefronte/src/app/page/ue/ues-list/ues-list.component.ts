import { Component, OnInit} from '@angular/core';
import $ from 'jquery';
import 'datatables.net';
import 'datatables.net-dt';
import { UejeffService } from '../../../services/UejeffService/uejeff.service';
import { Uejeff } from '../../../models/uejeff/uejeff';
import { CommonModule } from '@angular/common';
import { ProfesseurService } from '../../../services/ProfesseurService/professeur.service';
import { Professeur } from '../../../models/professeur/professeur';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Filiere } from '../../../models/filiere/filiere';
import { NiveauEtude } from '../../../models/enums/niveau-etude';
import { TypeSemestre } from '../../../models/enums/type-semestre';
import { FiliereService } from '../../../services/FiliereService/filiere.service';

@Component({
  selector: 'app-ues-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './ues-list.component.html',
  styleUrl: './ues-list.component.css'
})

export class UesListComponent implements OnInit {

  showSuccessMessage: boolean = false;
  ueForm!: FormGroup;
  selectedUE: Uejeff | undefined;
  ueIdToDelete: number | null = null;
  ueCodeToDelete: string = '';
  ueLibelleToDelete: string = '';
  professeurs: Professeur[] = [];
  filieres: Filiere[] = [];
  NiveauEtude = NiveauEtude;
  TypeSemestre = TypeSemestre;
  uejeffs: Uejeff[] = []
  ueId: any;
  route: any;
  constructor(
    private fb: FormBuilder,
    private uejeffService: UejeffService,
    private professeurService: ProfesseurService,
    private filiereService: FiliereService,
  ){
    this.ueForm = this.fb.group({
      code: ['', Validators.required],
      libelle: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
      credit: ['', [Validators.required, Validators.min(1)]],
      niveauEtude: ['', Validators.required],
      filiereIds: [[], Validators.required],
      typeSemestre: ['', Validators.required],
      professeurId: ['', Validators.required]  // Champ pour choisir le professeur
    });
  }

  modifier() {
    // Logique d'enregistrement ici (par exemple, envoyer un formulaire)

    // Afficher le message de succès
    this.showSuccessMessage = true;

    // Cacher le message après 3 secondes
    setTimeout(() => {
      this.showSuccessMessage = false;
    }, 3500);
  }

  ngOnInit(): void {

    this.loadUEs();
    this.professeurService.getProfesseurs().subscribe((data: Professeur[]) => {
      this.professeurs = data;
    });
    // Récupérer la liste des filières
    this.filiereService.getFilieres().subscribe((data: Filiere[]) => {
      this.filieres = data;
    });
  }

  initDataTable() {
    const table = $('#listeue');
    // Vérification si DataTable est déjà initialisé
    if (table.length > 0 && table.isDataTable()) {
      table.DataTable().clear().destroy(); // Si oui, on la détruit pour réinitialiser
    }

    // Initialisation de DataTable
    setTimeout(() => {
      if (table.length > 0) {
        table.DataTable({
          language: {
            url: 'https://cdn.datatables.net/plug-ins/1.13.5/i18n/fr-FR.json'
          },
          pagingType: 'full_numbers',
          destroy: true,
          retrieve: true
        });
      }
    }, 0);
  }




  loadUEs(){
    this.uejeffService.getUes().subscribe({
      next: (data: Uejeff[]) => {
        this.uejeffs = data;

        // Réinitialiser DataTable après le chargement des données
        this.initDataTable();
      },
      error: (error) => {
        console.error('Erreur lors du chargement des UE', error);
      },
      complete: () => {
        console.log('Chargement des UE terminé.');
      }
    });
  }


  openModifierModal(ueId: number): void {
    this.selectedUE = this.uejeffs.find(ue => ue.id === ueId);
    this.ueId = ueId; // Assigner l'ID de l'UE sélectionnée à ueId
    if (this.selectedUE) {
      // Pré-remplir le formulaire avec les données de l'UE sélectionnée
      this.ueForm.patchValue({

        code: this.selectedUE.code,
        libelle: this.selectedUE.libelle,
        type: this.selectedUE.type,
        description: this.selectedUE.description,
        credit: this.selectedUE.credit,
        niveauEtude: this.selectedUE.niveauEtude,
        filiereIds: this.selectedUE.filiereIds,
        typeSemestre: this.selectedUE.typeSemestre,
        professeurId: this.selectedUE.professeurId
      });
    }
  }


  onSubmit() {
  if (this.ueForm.valid && this.selectedUE) {
    const ueData = {
      ...this.ueForm.value,
      id: this.selectedUE.id  // Inclure l'ID dans le corps de la requête
    };

    this.uejeffService.updateUe(this.ueId, ueData).subscribe({
      next: (response) => {
        console.log('Réponse API après mise à jour :', response);  // Afficher la réponse complète
        if (response && response.id) {
          console.log('ID renvoyé après mise à jour :', response.id);
        } else {
          console.error('Aucun ID renvoyé dans la réponse après mise à jour');
        }
        window.location.reload();
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour de l\'UE', error);
      },
      complete: () => {
        setTimeout(() => this.initDataTable(), 0);  // Réinitialiser DataTable après rechargement
      }
    });
  }
}


 openDeleteModal(ue: Uejeff): void {
    this.ueIdToDelete = ue.id;  // Assigner l'ID de l'UE à supprimer
    this.ueCodeToDelete = ue.code;  // Assigner le code de l'UE à supprimer
    this.ueLibelleToDelete = ue.libelle;  // Assigner le libellé de l'UE à supprimer
  }
  // Méthode pour supprimer une UE

  confirmDelete(): void {
  if (this.ueIdToDelete !== null) {
    this.uejeffService.deleteUe(this.ueIdToDelete).subscribe({
      next: () => {
        console.log('UE supprimée avec succès');
        window.location.reload();  // Recharger la liste après suppression
      },
      error: (err) => {
        console.error('Erreur lors de la suppression de l\'UE', err);
      },
      complete: () => {
        setTimeout(() => this.initDataTable(), 0);  // Réinitialiser DataTable après suppression
      }
    });
    this.ueIdToDelete = null;
  }
 }
}
