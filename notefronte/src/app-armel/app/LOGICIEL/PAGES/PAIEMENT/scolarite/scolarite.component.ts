import { Component, Inject, OnInit, PLATFORM_ID, TemplateRef, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EtudiantService } from '../../../SERVICES/etudiant.service';
import { HttpClient } from '@angular/common/http';
import { Paiement } from '../../../MODELS/paiement';
import { Etudiant } from '../../../MODELS/etudiant';
import { PaiementService } from '../../../SERVICES/paiement.service';
import { NgForm } from '@angular/forms'; // Import NgForm
import { ElementRef } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { EcheanceService } from '../../../SERVICES/echeance.service';

@Component({
  selector: 'app-scolarite',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './scolarite.component.html',
  styleUrl: './scolarite.component.css'
})
export class ScolariteComponent implements OnInit {

  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements


  @ViewChild('closeModalButton') closeModalButton!: ElementRef;
  paiements: any[] = [];
  filieres: any[] = []; // Liste des filières
  niveaux: string[] = []; // Liste des niveaux
  selectedFiliere: string = '';
  selectedNiveau: string = '';
  selectedPrenom: string = '';
  selectedNom: string = '';
  nomsEtudiants: any[] = [];
  prenomsEtudiants: any[] = [];
  etudiants: Etudiant[] = [];
  isModalActive = false;
  isRenvoiButtonDisabled: boolean = true;

  private filieresUrl = 'http://localhost:8060/api/auth/comptable/filiere'; // URL pour récupérer les filières
  private niveauxUrl = 'http://localhost:8060/api/auth/comptable/niveau'; // URL pour récupérer les niveaux

  paiement = {
    etudiantNom: '',
    etudiantPrenom: '',
    montantActuel: null,
    datePaiement: '',
    resteEcolage: 0
  };


  showEditModal: boolean = false;
  selectedPaiement: Paiement | null = null;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private paiementService: PaiementService,
    private etudiantService: EtudiantService,
    private http: HttpClient,
    private modalService: NgbModal,
    private fb: FormBuilder,
    private echeanceService: EcheanceService,
    private router: Router
  ) { }


  ngOnInit(): void {
    this.loadFilieres();
    this.loadNiveaux();

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


  loadFilieres(): void {
    this.http.get<any[]>(this.filieresUrl).subscribe(
      data => {
        this.filieres = data;
      },
      error => {
        console.error('Erreur lors du chargement des filières:', error);
      }
    );
  }

  loadNiveaux(): void {
    this.http.get<string[]>(this.niveauxUrl).subscribe(
      data => {
        this.niveaux = data;
      },
      error => {
        console.error('Erreur lors du chargement des niveaux:', error);
      }
    );
  }




  onSearch(): void {
    if (!this.selectedFiliere || !this.selectedNiveau) {
      alert('Veuillez sélectionner une filière et un niveau.');
      return;
    }
    // Stocker les informations de la filière et du niveau dans le localStorage
  localStorage.setItem('selectedFiliere', this.selectedFiliere);
  localStorage.setItem('selectedNiveau', this.selectedNiveau);

    const url = `http://localhost:8060/api/auth/comptable/by-filiere-and-niveau?nomFiliere=${this.selectedFiliere}&niveauEtude=${this.selectedNiveau}`;
    this.http.get<Paiement[]>(url).subscribe(
      data => {
        this.paiements = data;
         // Stocker les paiements dans le localStorage
      localStorage.setItem('paiements', JSON.stringify(this.paiements));
      },
      error => {
        console.error('Erreur lors du chargement des paiements:', error);
      }
    );
    this.isRenvoiButtonDisabled = false;

  }

  // Méthode pour ouvrir le modal avec les détails d'un étudiant
  openDetails(content: TemplateRef<any>, paiement: any): void {
    this.selectedPaiement = paiement;
    this.modalService.open(content);

    console.log('Paiements:', this.paiements);
    this.paiements.forEach(paiement => {
  console.log('Echéances:', paiement.echeances);
});
  }


  onFiliereChange() {
    this.filterEtudiantsByFiliere();
  }

  filterEtudiantsByFiliere() {
    if (this.selectedFiliere) {
      const filteredEtudiants = this.etudiants.filter(etudiant => etudiant.nomFiliere === this.selectedFiliere);

      this.nomsEtudiants = Array.from(new Set(filteredEtudiants.map(etudiant => etudiant.etudiantNom)));
      this.prenomsEtudiants = Array.from(new Set(filteredEtudiants.map(etudiant => etudiant.etudiantPrenom)));
    } else {
      this.nomsEtudiants = [];
      this.prenomsEtudiants = [];
    }
  }

  onSubmit(form: NgForm): void {
      const confirmation = window.confirm('Êtes-vous sûr de vouloir soumettre ce paiement ?');

      if (
        confirmation && form.valid) {
        this.http.post('http://localhost:8060/api/auth/comptable/ajout_paiement', this.paiement)
          .subscribe({
            next: (response) => {
              console.log('Paiement ajouté avec succès', response);
              form.reset();
              this.onSearch();
              this.paiement = {
                etudiantNom: '',
                etudiantPrenom: '',
                montantActuel: null,
                datePaiement: '',
                resteEcolage: 0
              };
              this.closeModal();
              alert(' Paiement ajouté avec succès.');

            },
            error: (error) => {
              console.error('Erreur lors de l\'ajout du paiement:', error);
              alert('Erreur : Verifier le montant que vous venez de saisir;  sa valeur ne doit pas dépasser le RESTE ni etre NEGATIF.');            }
          });
      }
    }


  closeModal() {
    if (isPlatformBrowser(this.platformId) && this.closeModalButton) {
      this.closeModalButton.nativeElement.click();
    }
  }



  closeEditModal() {
    this.showEditModal = false;
    this.selectedPaiement = null; // Réinitialiser après la fermeture si nécessaire
  }





  openModal(paiement: any) {
    this.selectedPaiement = paiement;  // Enregistre les infos de l'étudiant sélectionné
    this.isModalActive = true;  // Active la modal
  }

  closModal() {
    this.isModalActive = false;  // Désactive la modal
  }
  selectEtudiant(paiement: any) {
    this.paiement.etudiantNom = paiement.etudiantNom;
    this.paiement.etudiantPrenom = paiement.etudiantPrenom;
    // Optionnel : réinitialiser d'autres champs si nécessaire
    this.paiement.montantActuel = null;

  }

  onEditSubmit(form: NgForm) {
    const { etudiantNom, etudiantPrenom, montantActuel, datePaiement } = form.value;

    this.paiementService.updatePaiement(etudiantNom, etudiantPrenom, montantActuel, datePaiement)
      .subscribe(
        response => {
          console.log('Paiement mis à jour avec succès', response);
          window.alert('Paiement modifié avec succès !'); // Afficher un message de confirmation
          form.reset(); // Réinitialiser le formulaire
        },
        error => {
          console.error('Erreur lors de la mise à jour du paiement', error);
          window.alert('Erreur lors de la mise à jour du paiement. Veuillez réessayer.'); // Afficher un message d'erreur
        }
      );
  }

  openEditModal(paiement: Paiement) {
    this.selectedPaiement = paiement;
    this.showEditModal = true;

  }

  // Méthode pour rediriger
  redirectToFicheRenvoi() {
    this.router.navigate(['/app-armel/fiche-de-renvoi']);

  }




}




