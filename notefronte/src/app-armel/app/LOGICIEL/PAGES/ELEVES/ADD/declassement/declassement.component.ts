import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Etudiant } from '../../../../MODELS/etudiant';
import { EnumsService } from '../../../../SERVICES/enums.service';
import { EtudiantService } from '../../../../SERVICES/etudiant.service';
import { FiliersService } from '../../../../SERVICES/filiers.service';
import { ParcourService } from '../../../../SERVICES/parcour.service';
import { Router } from '@angular/router';
import { Paiement } from '../../../../MODELS/paiement';
import { EcheanceService } from '../../../../SERVICES/echeance.service';

@Component({
  selector: 'app-declassement',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './declassement.component.html',
  styleUrls: ['./declassement.component.css']
})
export class DeclassementComponent implements OnInit {
  parcoursList: any[] = [];             
  niveauxDisponibles: string[] = [];    
  statutBoursier: string[] = [];
  mentions: string[] = [];
  modalites: string[] = [];
  filiereList: any[] = [];
  nomParcours: string = '';
  apiBaseUrl = 'http://localhost:4200/parcours'; 

  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  
  etudiant: Etudiant = {
    etudiantMatricule: '',
    etudiantNom: '',
    etudiantPrenom: '',
    etudiantAdresse: '',
    etudiantTelephone: '',
    etudiantMail: '',
    etudiantDateNaiss: '',
    etudiantLieuNais: '',
    etudiantNationnalite: '',
    etudiantSexe: '',
    etudiantSerieBac: '',
    etudiantAnneeBac: '',
    etudiantPaysBac: '',
    etudiantEtatProvenance: '',
    etudiantAutreDiplome: '',
    etudiantDateIns: '',
    mentionBac: '',
    boursier: '',
    parcoursId: 0,
    nomFiliere: '',
    niveauEtude: '',
    typeModalite: '',
    tuteurNom: '',
    tuteurPrenom: '',
    tuteurProfession: '',
    tuteurOrganismeEmployeur: '',
    tuteurAdresse: '',
    tuteurTelBureau: '',
    tuteurTelDom: '',
    tuteurCel: '',
    tuteurEmail: ''
  };

  private apiUrl = 'http://localhost:8060/api/comptable/update_etudiant';

  constructor(
    private filiersService: FiliersService,
    private enumsService: EnumsService,
    private parcourService: ParcourService,
    private etudiantService: EtudiantService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private echeanceService: EcheanceService

  ) {}

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

    console.log(this.route.snapshot.paramMap);

    // Récupérer les parcours
    this.parcourService.getParcours().subscribe(
      (data) => {
        this.parcoursList = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des parcours', error);
      }
    );

    const matricule = this.route.snapshot.paramMap.get('matricule'); // Récupérer le matricule de l'URL
  if (matricule) {
    this.etudiantService.getEtudiantByMatricule(matricule).subscribe(
      (data) => {
        this.etudiant = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération de l\'étudiant:', error);
      }
    );
  } else {
    console.error('Aucun matricule fourni');
  }

    // Récupérer les modalités
    this.enumsService.getModalites().subscribe(
      (data) => {
        this.modalites = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des modalités', error);
      }
    );

    // Récupérer les mentions
    this.enumsService.getMentions().subscribe(
      (data) => {
        this.mentions = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des mentions', error);
      }
    );

    // Récupérer les statuts boursiers
    this.enumsService.getStatutBoursier().subscribe(
      (data) => {
        this.statutBoursier = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des statuts boursiers', error);
      }
    );
  }



  // Soumission du formulaire pour la mise à jour d'un étudiant
  updateEtudiant(): void {
    const confirmation = confirm('Êtes-vous sûr de vouloir mettre à jour cet étudiant ?');
    
    if (confirmation) {
      this.http.put(`${this.apiUrl}/${this.etudiant.etudiantMatricule}`, this.etudiant).subscribe(
        () => {
          console.log('Étudiant mis à jour avec succès');
          // Redirection vers la page de l'étudiant
          this.closeForm();      
          this.router.navigate(['/parcours']);
        },
        (error) => {
          console.error('Erreur lors de la mise à jour de l\'étudiant:', error);
        }
      );
    } else {
      console.log('Mise à jour annulée');
    }
  }
  closeForm(): void {
    // Logic to close the form, e.g., resetting fields or hiding modal
    this.etudiant = { // Resetting to initial state if needed
      etudiantMatricule: '',
      etudiantNom: '',
      etudiantPrenom: '',
      etudiantAdresse: '',
      etudiantTelephone: '',
      etudiantMail: '',
      etudiantDateNaiss: '',
      etudiantLieuNais: '',
      etudiantNationnalite: '',
      etudiantSexe: '',
      etudiantSerieBac: '',
      etudiantAnneeBac: '',
      etudiantPaysBac: '',
      etudiantEtatProvenance: '',
      etudiantAutreDiplome: '',
      etudiantDateIns: '',
      mentionBac: '',
      boursier: '',
      parcoursId: 0,
      nomFiliere: '',
      niveauEtude: '',
      typeModalite: '',
      tuteurNom: '',
      tuteurPrenom: '',
      tuteurProfession: '',
      tuteurOrganismeEmployeur: '',
      tuteurAdresse: '',
      tuteurTelBureau: '',
      tuteurTelDom: '',
      tuteurCel: '',
      tuteurEmail: ''
    };
  }

  onParcoursChange(event: any): void {
    const parcoursId = event.target.value;
    // Récupérer les filières associées au parcours via le service
    this.filiersService.getFilieresByParcoursId(parcoursId).subscribe(
      (data) => {
        this.filiereList = data; // Mettre à jour la liste des filières avec les données récupérées
      },
      (error) => {
        console.error('Erreur lors de la récupération des filières :', error); // Gérer l'erreur en cas d'échec
      }
    );
     // Récupérer les niveaux associés au parcours sélectionné
     this.getNiveauxByParcours(parcoursId);
  }
  // Méthode pour récupérer les niveaux d'étude en fonction de l'ID du parcours
  getNiveauxByParcours(parcoursId: number): void {
    this.parcourService.getNiveauxByParcours(parcoursId).subscribe(
      (data: string[]) => {
        this.niveauxDisponibles = data;  // Mettre à jour les niveaux disponibles
      },
      (error) => {
        console.error('Erreur lors de la récupération des niveaux :', error);
      }
    );
  }
  }
