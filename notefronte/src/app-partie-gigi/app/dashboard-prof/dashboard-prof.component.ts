import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { PopupComponent } from '../popup/popup.component';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { LoginService } from '../../../app-connexion/app/service/login.service';

@Component({
  selector: 'app-dashboard-prof',
  standalone: true,
  imports: [MatSidenavModule, MatSnackBarModule, CommonModule, MatIconModule, HttpClientModule],
  templateUrl: './dashboard-prof.component.html',
  styleUrls: ['./dashboard-prof.component.css']
})
export class DashboardProfComponent implements OnInit {
  data: string | null = null;
  currentView: string = 'grid'; // Valeur par défaut
  courses: any[] = [];
  selectedUeId: number | null = null;
  userId: number = 0;

  constructor(private router: Router, public dialog: MatDialog, private http: HttpClient, private authCo: LoginService) {}

  ngOnInit() {
    this.userId = this.authCo.getUserId();
    // Simuler une récupération de données avec un délai
    setTimeout(() => {
      this.data = "Ahh hs";
    }, 3000); // Simule un délai de 3 secondes

    this.fetchCourses();
  }

  get content() {
    return this.data;
  }

  fetchCourses() {
    const professeurId = localStorage.getItem("professeurId"); // Exemple d'ID du professeur
    this.http.get<any[]>(`http://localhost:8060/api/auth/professeur/courbyprofesseur/${professeurId}`)
      .subscribe(
        (courses: any[]) => {
          this.courses = courses;
        },
        (error) => {
          console.error('Erreur lors de la récupération des cours:', error);
        }
      );
  }

  toggleView(view: string) {
    this.currentView = view;
  }


  openPopup(ueId: number): void {
    const dialogRef = this.dialog.open(PopupComponent, {
      data: { ueId: ueId }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Le dialogue a été fermé');
      if (result === 'selected') {
        // Stocker l'UE sélectionnée dans localStorage
        localStorage.setItem('ueInfo', JSON.stringify({ ueId: ueId }));
      }
    });
  }


  onItemClick(route: string): void {
    this.router.navigate([route]);
  }
}
