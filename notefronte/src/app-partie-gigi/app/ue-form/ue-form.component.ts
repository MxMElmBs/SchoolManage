// ue-form.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

interface Professor {
  professeurId: number;
  nom: string;
  prenom: string;
}

interface Filiere {
  id: number;
  nomFiliere: string;
}

@Component({
  selector: 'app-ue-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './ue-form.component.html',
  styleUrls: ['./ue-form.component.css']
})
export class UeFormComponent implements OnInit {
  ueForm: FormGroup;
  professors: Professor[] = [];
  filieres: Filiere[] = [];
  niveauxEtude: string[] = [];
  typesSemestre: string[] = [];
  selectedFilieres: number[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.ueForm = this.fb.group({
      libelle: ['', Validators.required],
      code: ['', Validators.required],
      credit: ['', [Validators.required, Validators.min(1)]],
      quotaSemaine: ['', [Validators.required, Validators.min(1)]],
      descriptUe: ['', Validators.required],
      professeur: ['', Validators.required],
      niveauEtude: ['', Validators.required],
      typeSemestre: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.fetchProfessors();
    this.fetchFilieres();
    this.fetchNiveauEtude();
    this.fetchTypeSemestre();
  }

  onFiliereChange(event: any) {
    const value = Number(event.target.value);
    if (event.target.checked) {
      this.selectedFilieres.push(value);
    } else {
      const index = this.selectedFilieres.indexOf(value);
      if (index > -1) {
        this.selectedFilieres.splice(index, 1);
      }
    }
  }

  fetchProfessors() {
    this.http.get<Professor[]>('http://localhost:8060/api/auth/de/professors')
      .subscribe((data: Professor[]) => {
        this.professors = data;
      }, error => {
        console.error('Erreur lors de la récupération des professeurs:', error);
        Swal.fire('Erreur', 'Impossible de récupérer les professeurs.', 'error');
      });
  }

  fetchFilieres() {
    const parcoursId = localStorage.getItem('parcoursId');
    this.http.get<Filiere[]>(`http://localhost:8060/api/auth/de/filieres/${parcoursId}`)
      .subscribe((data: Filiere[]) => {
        this.filieres = data;
      }, error => {
        console.error('Erreur lors de la récupération des filières:', error);
        Swal.fire('Erreur', 'Impossible de récupérer les filières.', 'error');
      });
  }

  fetchNiveauEtude() {
    this.http.get<string[]>('http://localhost:8060/api/auth/de/enums/niveau-etude')
      .subscribe((data: string[]) => {
        this.niveauxEtude = data;
      }, error => {
        console.error('Erreur lors de la récupération des niveaux d\'étude:', error);
        Swal.fire('Erreur', 'Impossible de récupérer les niveaux d\'étude.', 'error');
      });
  }

  fetchTypeSemestre() {
    this.http.get<string[]>('http://localhost:8060/api/auth/de/enums/type-semestre')
      .subscribe((data: string[]) => {
        this.typesSemestre = data;
      }, error => {
        console.error('Erreur lors de la récupération des types de semestre:', error);
        Swal.fire('Erreur', 'Impossible de récupérer les types de semestre.', 'error');
      });
  }

  soumettre() {
    if (this.ueForm.valid) {
      const ueData = {
        libelle: this.ueForm.value.libelle,
        code: this.ueForm.value.code,
        credit: this.ueForm.value.credit,
        quotaSemaine: this.ueForm.value.quotaSemaine,
        descriptUe: this.ueForm.value.descriptUe,
        professeur: {
          professeurId: this.ueForm.value.professeur
        },
        filieres: this.selectedFilieres.map(id => ({ id })),
        niveauEtude: this.ueForm.value.niveauEtude,
        typeSemestre: this.ueForm.value.typeSemestre,
        etat: 'NORMALE',
        heureEffectuer: 0
      };
  
      console.log('Données à envoyer :', ueData);
  
      // Afficher la boîte de dialogue "Veuillez patienter"
      Swal.fire({
        title: 'Ajout en cours',
        text: 'Veuillez patienter...',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });
  
      // Envoyer la requête HTTP avec le type de réponse spécifié comme 'text'
      this.http.post('http://localhost:8060/api/auth/de/add-cours', ueData, { responseType: 'text' })
        .subscribe(response => {
          try {
            // Essayer de parser la réponse comme JSON
            const jsonResponse = JSON.parse(response);
            console.log('Réponse JSON du serveur:', jsonResponse);
            
            // Si la réponse est un JSON, afficher un message de succès
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: 'Cours ajouté avec succès!'
            });
          } catch (e) {
            // Si la réponse n'est pas un JSON, traiter comme du texte et afficher un message de succès
            console.log('Réponse texte du serveur:', response);
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: response || 'Cours ajouté avec succès!'
            });
          }
          
          // Réinitialiser le formulaire après l'ajout réussi
          this.ueForm.reset();
          this.selectedFilieres = [];
        }, error => {
          console.error('Erreur:', error);
          const errorMessage = error.error?.message || 'Une erreur est survenue lors de l\'ajout du cours!';
          
          // Afficher une alerte en cas d'erreur
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: errorMessage
          });
        });
    } else {
      // Afficher un message d'avertissement si le formulaire n'est pas valide
      Swal.fire({
        icon: 'warning',
        title: 'Attention',
        text: 'Veuillez remplir tous les champs requis!'
      });
    }
  }
  
}
