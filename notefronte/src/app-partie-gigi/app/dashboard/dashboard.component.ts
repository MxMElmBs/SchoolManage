import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgOptimizedImage } from '@angular/common';
import { MatBadgeModule } from '@angular/material/badge';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../../app-connexion/app/service/login.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterOutlet, CommonModule, MatSidenavModule, RouterLink, MatSnackBarModule, NgOptimizedImage, MatBadgeModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  studentsMenuOpen = false; // Ensure this variable exists
  directorData: any | null = null; // To hold director data

  constructor(private router: Router, private http: HttpClient, private authCo : LoginService) {}

  ngOnInit() {
    // Retrieve the userId from local storage
    const userId = this.authCo.getUserId();
    if (userId) {
      this.http.get<any>(`http://localhost:8060/api/auth/de/directeurInfo/${userId}`)
        .subscribe({
          next: (directeurEtudeDto) => {
            this.directorData = directeurEtudeDto;
            console.log('Directeur data:', directeurEtudeDto);
            localStorage.setItem('parcoursId', directeurEtudeDto.parcoursId.toString());
            localStorage.setItem('directeurEtudeId', directeurEtudeDto.directeurEtudeId.toString());
          },
          error: (error) => {
            console.error('Error fetching directeur data', error);
          }
        });
    } else {
      console.error('No userId found in local storage');
    }
  }

  search() {
    // Implement search logic here
    console.log('Search clicked');
  }

  onItemClick(route: string): void {
    this.router.navigate([route]);
  }

  isActive(route: string): boolean {
    return this.router.url === route;
  }

  toggleStudentsMenu() {
    // Toggle the state of the menu
    this.studentsMenuOpen = !this.studentsMenuOpen;
  }

  onSubmenuItemClick() {
    // Keep the menu open
    this.studentsMenuOpen = true; // Force the menu to stay open
  }

  logout() {
    this.router.navigate(['/login']);
  }

  hidden = false;

  toggleBadgeVisibility() {
    this.hidden = !this.hidden;
  }
}
