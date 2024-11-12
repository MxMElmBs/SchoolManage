import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { EnumsService } from "../../../../SERVICES/enums.service";
import { EtudiantService } from "../../../../SERVICES/etudiant.service";
import { FiliersService } from "../../../../SERVICES/filiers.service";
import { ParcourService } from "../../../../SERVICES/parcour.service";
import { Etudiant } from "../../../../MODELS/etudiant";
import { HttpClient } from "@angular/common/http";
import { NiveauService } from "../../../../SERVICES/niveau.service";
import { Paiement } from "../../../../MODELS/paiement";
import { EcheanceService } from "../../../../SERVICES/echeance.service";

@Component({
  selector: 'app-add',
  standalone: true,
  imports: [CommonModule,
    FormsModule,
    RouterLink],
  templateUrl: './add.component.html',
  styleUrl: './add.component.css'
})
export class AddComponent implements OnInit {
  parcoursList: any[] = [];
  niveauxDisponibles: string[] = [];
  statutBoursier: string[] = [];
  mentions: string[] = [];
  modalites: string[] = [];
  filiereList: any[] = [];
  nomParcours: string = '';
  rappels: Paiement[] = [];
  loading = true;
  error: string | null = null;
  nombreRappels: number = 0; // Variable pour stocker le nombre de paiements

  etudiant: Etudiant = {
    etudiantMatricule:'',
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

  apiUrl: string = 'http://localhost:8060/api/auth/comptable/ajout_etudiant';


  constructor(
    private filiersService: FiliersService,
    private enumsService: EnumsService,
    private parcourService: ParcourService,
    private etudiantService: EtudiantService,
    private niveauService: NiveauService,
    private http: HttpClient,
    private echeanceService: EcheanceService,
    private route: ActivatedRoute) { }





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

    // Appel du service pour récupérer les parcours
    this.parcourService.getParcours().subscribe(
      (data) => {
        this.parcoursList = data; // Assigner les données reçues à la liste
      },
      (error) => {
        console.error('Erreur lors de la récupération des parcours', error);
      }
    );


    this.enumsService.getModalites().subscribe(
      (data) => {
        this.modalites = data; // Assigner les données reçues à la liste
      },
      (error) => {
        console.error('Erreur lors de la récupération des modalites', error);
      }
    )




    this.enumsService.getMentions().subscribe(
      (data) => {
        this.mentions = data; // Assigner les données reçues à la liste
      },
      (error) => {
        console.error('Erreur lors de la récupération des mentions', error);
      }
    )


    this.enumsService.getStatutBoursier().subscribe(
      (data) => {
        this.statutBoursier = data; // Assigner les données reçues à la liste
      },
      (error) => {
        console.error('Erreur lors de la récupération des Bourses', error);
      }
    );


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


  onSubmit() {
    // Créez un tableau avec uniquement les champs obligatoires
    const requiredFields = [
        { field: this.etudiant.etudiantNom, name: "Nom" },
        { field: this.etudiant.etudiantPrenom, name: "Prénom" },
        { field: this.etudiant.etudiantMail, name: "Email" },
        { field: this.etudiant.etudiantAdresse, name: "Adresse" },
        { field: this.etudiant.etudiantTelephone, name: "Téléphone" },
        { field: this.etudiant.etudiantDateNaiss, name: "Date de Naissance" },
        { field: this.etudiant.etudiantSexe, name: "Sexe" },
        { field: this.etudiant.etudiantLieuNais, name: "Lieu de naissance" },
        { field: this.etudiant.etudiantNationnalite, name: "Nationnalite" },

        { field: this.etudiant.etudiantSerieBac, name: "Série Bac" },
        { field: this.etudiant.mentionBac, name: "Mention Bac" },
        { field: this.etudiant.parcoursId, name: "Parcours ID" },
        { field: this.etudiant.nomFiliere, name: "Nom Filière" },
        { field: this.etudiant.niveauEtude, name: "Niveau d'Étude" },
        { field: this.etudiant.typeModalite, name: "Type Modalité" },
    ];

    // Filtrer pour trouver les champs qui sont vides
    const missingFields = requiredFields.filter(item =>
        (typeof item.field === 'string' && item.field.trim() === '') || // Vérifie si c'est une chaîne et utilise trim
        (typeof item.field === 'number' && item.field === 0) // Vérifie si c'est un nombre
    );

    if (missingFields.length > 0) {
        alert("Veuillez remplir les champs obligatoires : " + missingFields.map(item => item.name).join(", "));
        return; // Ne pas continuer si des champs requis ne sont pas remplis
    }

    // Construction de la chaîne récapitulative
    const recap = requiredFields
        .map(item => `${item.name}: ${item.field || 'Non renseigné'}`)
        .join('\n');

    // Affichage de la boîte de confirmation avec toutes les informations
    const confirmation = window.confirm(`Voici les informations que vous allez ajouter :\n\n${recap}\n\nVoulez-vous continuer ?`);

    if (confirmation) {
        console.log('Objet étudiant avant l\'envoi:', this.etudiant);

        // Envoyer les données au serveur
        this.http.post(this.apiUrl, this.etudiant)
            .subscribe(response => {
                console.log(this.etudiant);
                console.log('Étudiant ajouté avec succès', response);

                if (response) {
                    alert('Étudiant ajouté avec succès.');
                    this.resetEtudiant(); // Réinitialiser le formulaire
                }
            }, error => {
                console.log(this.etudiant);

                // Afficher les messages d'erreur du backend
                if (error.status === 409) {
                    alert('Erreur : Étudiant déjà ajouté.'); // Message spécifique pour le conflit
                } else if (error.error) {
                    alert('Erreur : ' + error.error); // Autres messages d'erreur spécifiques
                }
            });
    }
}







  // Méthode pour réinitialiser le formulaire
  resetEtudiant(): void {
    this.etudiant = {
      etudiantMatricule:'',
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

}
