import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { OtherUserService } from '../../services/secre/otheruser.service';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-acceuil',
  standalone: true,
  imports: [CommonModule, MatIconModule, DashboardComponent, HeaderComponent, FooterComponent],
  templateUrl: './acceuil.component.html',
  styleUrls: ['./acceuil.component.css'] // Correction de 'styleUrl' en 'styleUrls'
})
export class AcceuilComponent /*implements OnInit */{
  etudiantCount: number = 0;
  comptaCount: number = 0;
  profCount: number = 0;
  secretaireCount: number = 0;
  directeurCount: number = 0;

  cards = [
    { name: 'Étudiants', color: '#0029FF', icon: 'ri-user-line', count: this.etudiantCount },
    { name: 'Enseignants', color: '#01009A', icon: 'ri-book-open-line', count: this.profCount },
    { name: 'Directeurs d\'étude', color: '#9D8222', icon: 'ri-team-line', count: this.directeurCount },
    { name: 'Comptables', color: '#2C9D22', icon: 'ri-wallet-3-line', count: this.comptaCount }
  ];


  constructor(private comptaService: OtherUserService, private userService: UserService) {}

  ngOnInit(): void {
    this.getEtudiantCount();
    this.getProfCount();
    this.getDECount();
    this.getComptaCount();
  }

  getComptaCount(): void {
    this.userService.countByRole('COMPTABLE').subscribe(
      (count: number) => {
        this.comptaCount = count;
        this.updateCard('Comptables', count);
      },
      error => {
        console.error('Error fetching Comptable count:', error);
      }
    );
  }



  getEtudiantCount(): void {
    this.userService.countByRole('ETUDIANT').subscribe(
      (count: number) => {
        this.etudiantCount = count;
        this.updateCard('Étudiants', count);
      },
      error => {
        console.error('Error fetching Étudiant count:', error);
      }
    );
  }

  getDECount(): void {
    this.userService.countByRole('DE').subscribe(
      (count: number) => {
        this.directeurCount = count;
        this.updateCard('Directeurs d\'étude', count);
      },
      error => {
        console.error('Error fetching Directeur d\'étude count:', error);
      }
    );
  }

  getProfCount(): void {
    this.userService.countByRole('PROFESSEUR').subscribe(
      (count: number) => {
        this.profCount = count;
        this.updateCard('Enseignants', count);
      },
      error => {
        console.error('Error fetching Professeur count:', error);
      }
    );
  }

  // Méthode pour mettre à jour le tableau des cartes
  private updateCard(roleName: string, count: number): void {
    const card = this.cards.find(card => card.name === roleName);
    if (card) {
      card.count = count;
    }
  }
}
