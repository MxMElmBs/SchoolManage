import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatCard } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2'; // Importer SweetAlert2
import { CommonModule } from '@angular/common';

export interface Etudiant {
  id: number;
  matricule: string;
  nom: string;
  prenom: string;
  tauxPresence: number;
}

@Component({
  selector: 'app-classedetails',
  standalone: true,
  imports: [CommonModule, MatCard, MatTableModule],
  templateUrl: './classedetails.component.html',
  styleUrls: ['./classedetails.component.css']
})
export class ClassedetailsComponent implements OnInit {
  ueId: string | null = null;
  libelleUe: string | null = null; // Pour stocker le libelle de l'UE
  codeUe: string | null = null;
  etudiants: Etudiant[] = [];
  displayedColumns: string[] = ['matricule', 'nom', 'prenom', 'tauxPresence', 'action'];
  isLoading = false; // Variable pour gérer l'affichage du spinner de chargement

  constructor(
    private http: HttpClient, 
    private route: ActivatedRoute, 
    private router: Router
  ) { }

  ngOnInit(): void {
    this.ueId = this.route.snapshot.paramMap.get('ueId');
    if (this.ueId) {
      this.getUeDetails(this.ueId);
      this.getEtudiants();
    } else {
      console.error('ueId est manquant ou incorrect');
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'ID de l\'UE manquant ou incorrect.',
        confirmButtonText: 'OK'
      });
    }
  }
  
  getUeDetails(ueId: string): void {
    const apiUrl = `http://localhost:8060/api/auth/de/libUe/${ueId}`; 
    this.http.get<{ libelle: string, code: string }>(apiUrl).subscribe(
      (data) => {
        this.libelleUe = data.libelle;
        this.codeUe = data.code;
      },
      (error) => {
        console.error('Erreur lors de la récupération des détails de l\'UE', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Une erreur est survenue lors de la récupération des détails de l\'UE.',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  getEtudiants(): void {
    if (this.ueId) {
      this.isLoading = true;
      const apiUrl = `http://localhost:8060/api/auth/de/ue/${this.ueId}/attendanceRates`;
      this.http.get<Etudiant[]>(apiUrl).subscribe(
        (data) => {
          this.etudiants = data.map(etudiant => ({
            id: etudiant.id,
            matricule: etudiant.matricule,
            nom: etudiant.nom,
            prenom: etudiant.prenom,
            tauxPresence: etudiant.tauxPresence
          }));
          this.isLoading = false;
        },
        (error) => {
          console.error('Erreur lors de la récupération des données', error);
          this.isLoading = false;
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: error.error?.message || 'Une erreur est survenue lors de la récupération des données.',
            confirmButtonText: 'OK'
          });
        }
      );
    } else {
      console.error('ueId est manquant ou incorrect');
    }
  }
  

  // Méthode pour signaler une absence à l'API backend
  signalerAbsence(etudiant: Etudiant) {
    const apiUrl = `http://localhost:8060/api/auth/de/signaler/${etudiant.id}`;
    Swal.fire({
      title: 'Signaler une absence',
      text: `Êtes-vous sûr de vouloir signaler l'absence pour ${etudiant.nom} ${etudiant.prenom} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, signaler',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true; // Activer le spinner pendant l'appel API
        this.http.post<void>(apiUrl, {}).subscribe(
          () => {
            this.isLoading = false; // Désactiver le spinner après succès
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: `L'absence de ${etudiant.nom} ${etudiant.prenom} a été signalée.`,
              confirmButtonText: 'OK'
            });
          },
          (error) => {
            this.isLoading = false; // Désactiver le spinner en cas d'erreur
            console.error('Erreur lors du signalement de l\'absence', error);
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: `Une erreur est survenue lors du signalement de l'absence pour ${etudiant.nom} ${etudiant.prenom}.`,
              confirmButtonText: 'OK'
            });
          }
        );
      }
    });
  }
}
