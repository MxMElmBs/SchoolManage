import { Component, OnInit } from '@angular/core';
import { UesListComponent } from './ues-list/ues-list.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Professeur } from '../../models/professeur/professeur';
import { ProfesseurService } from '../../services/ProfesseurService/professeur.service';
import { UejeffService } from '../../services/UejeffService/uejeff.service';
import { Uejeff } from '../../models/uejeff/uejeff';
import { FiliereService } from '../../services/FiliereService/filiere.service';
import { Filiere } from '../../models/filiere/filiere';
import { NiveauEtude } from '../../models/enums/niveau-etude';
import { TypeSemestre } from '../../models/enums/type-semestre';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-ue',
  standalone: true,
  imports: [CommonModule, UesListComponent, FormsModule, ReactiveFormsModule],
  templateUrl: './ue.component.html',
  styleUrl: './ue.component.css'
})
export class UeComponent implements OnInit {
  ueForm!: FormGroup;
  professeurs: Professeur[] = [];
  filieres: Filiere[] = [];
  NiveauEtude = NiveauEtude;
  TypeSemestre = TypeSemestre;
  showSuccessMessage: boolean = false;

  constructor(
    private fb: FormBuilder,
    private uejeffService: UejeffService,
    private professeurService: ProfesseurService,
    private filiereService: FiliereService
  ) {
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
  ngOnInit(): void {

    // Récupérer la liste des professeurs
    this.professeurService.getProfesseurs().subscribe((data: Professeur[]) => {
      this.professeurs = data;
    });
    // Récupérer la liste des filières
    this.filiereService.getFilieres().subscribe((data: Filiere[]) => {
      this.filieres = data;
    });
  }

  // Méthode pour soumettre le formulaire et ajouter une UE
  onSubmit(): void {
    console.log(this.ueForm.value); // Voir les valeurs du formulaire avant de les envoyer

    if (this.ueForm.valid) {
      const newUe: Uejeff = this.ueForm.value;
      console.log('Données envoyées :', newUe);

      this.uejeffService.createUe(newUe).subscribe({
        next: (response) => {
          console.log('Réponse du serveur :', response);

          // Afficher SweetAlert pour indiquer que l'ajout a réussi
          Swal.fire({
            title: 'Succès!',
            text: 'L\'UE a été ajoutée avec succès.',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            // Rafraîchir la page après avoir appuyé sur le bouton "OK"
            window.location.reload();
          });

          this.ueForm.reset();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout de l\'UE :', error);

          // Afficher SweetAlert en cas d'erreur
          Swal.fire({
            title: 'Erreur!',
            text: 'Un problème est survenu lors de l\'ajout de l\'UE.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    } else {
      // Si le formulaire est invalide, afficher une alerte SweetAlert
      Swal.fire({
        title: 'Formulaire Invalide',
        text: 'Veuillez vérifier tous les champs et réessayer.',
        icon: 'warning',
        confirmButtonText: 'OK'
      });
    }
  }



}
