import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Assurez-vous d'avoir FormsModule pour ngModel

interface CahierTexte {
  heureDebut: string;
  heureFin: string;
  contenuSeance: string;
}

@Component({
  selector: 'app-text-book',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './text-book.component.html',
  styleUrls: ['./text-book.component.css']
})
export class TextBookComponent implements OnInit {
  heureDebut: string = '';
  heureFin: string = '';
  contenu: string = '';
  seanceId: number | null = null;
  isProfesseur: boolean = false;

  apiUrlEtudiant = 'http://localhost:8060/api/auth/etudiant/enregistrer-cahier';
  apiUrlProfesseur = 'http://localhost:8060/api/auth/professeur/';
  apiUrlUpdate = 'http://localhost:8060/api/auth/professeur/updateOrConfirmercahiertexte/';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.isProfesseur = this.router.url.includes('/professeur');
    this.route.paramMap.subscribe(params => {
      this.seanceId = +params.get('seanceId')! || Number(localStorage.getItem('seanceId'));
      if (this.isProfesseur && this.seanceId) {
        this.getCahierDetails();
      }
      console.log("seanceId : " +this.seanceId);
    });
  }

  // Fonction pour récupérer les détails du cahier de texte pour un professeur
  getCahierDetails() {
    if (this.seanceId) {
      this.http.get<CahierTexte>(`${this.apiUrlProfesseur}cahier-details/${this.seanceId}`).subscribe(
        (data: CahierTexte) => {
          this.heureDebut = data.heureDebut;
          this.heureFin = data.heureFin;
          this.contenu = data.contenuSeance;
        },
        error => {
          console.error('Erreur lors de la récupération des détails du cahier de texte', error);
        }
      );
    }
  }

  // Fonction pour soumettre le formulaire (ajout ou modification)
  onSubmit() {
    if (this.isProfesseur) {
      // Le professeur peut modifier le cahier existant
      this.updateCahier();
    } else {
      // Si l'utilisateur est un étudiant, ajouter un nouveau cahier
      this.ajouterUE();
    }
  }

  // Fonction pour ajouter un cahier de texte (étudiant)
  ajouterUE() {
    const cahierTexteDTO = {
      heureDebut: this.heureDebut,
      heureFin: this.heureFin,
      contenuSeance: this.contenu,
      seanceId: this.seanceId
    };

    this.http.post(`${this.apiUrlEtudiant}/${this.seanceId}`, cahierTexteDTO).subscribe(
      response => {
        this.snackBar.open('Cahier de texte créé avec succès', 'Fermer', { duration: 3000 });
        this.router.navigate(['/app-gigi/studentdash/textbookhisto']); // Redirection après succès
      },
      error => {
        console.error('Erreur lors de la création du cahier de texte', error);
      }
    );
  }

  // Fonction pour modifier et approuver le cahier de texte (professeur)
  updateCahier() {
    if (this.seanceId) {
      const cahierTexteDTO = {
        heureDebut: this.heureDebut,
        heureFin: this.heureFin,
        contenuSeance: this.contenu
      };

      this.http.put(`${this.apiUrlUpdate}${this.seanceId}`, cahierTexteDTO).subscribe(
        response => {
          this.snackBar.open('Cahier de texte mis à jour avec succès', 'Fermer', { duration: 3000 });
          this.router.navigate(['/app-gigi/professeur/salle']); // Redirection après succès
        },
        error => {
          console.error('Erreur lors de la mise à jour du cahier de texte', error);
        }
      );
    }
  }
  
}
