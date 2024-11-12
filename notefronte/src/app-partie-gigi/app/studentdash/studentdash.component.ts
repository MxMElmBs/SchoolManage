import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgOptimizedImage } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { EtudiantConnect } from '../../../app-students-interfaces/app/models/EtudiantConnect';
import { LoginService } from '../../../app-connexion/app/service/login.service';

@Component({
  selector: 'app-studentdash',
  standalone: true,
  imports: [RouterOutlet, CommonModule, MatSidenavModule, RouterLink, MatSnackBarModule, NgOptimizedImage],
  templateUrl: './studentdash.component.html',
  styleUrls: ['./studentdash.component.css'] // Fixed typo from styleUrl to styleUrls
})
export class StudentdashComponent implements OnInit {
  studentData: EtudiantConnect | null = null;

  constructor(private router: Router, private http: HttpClient, private authCo : LoginService) {}

  ngOnInit() {
    const userId = this.authCo.getUserId(); // Make sure 'userId' is the correct key

    if (userId) {
      // Make the HTTP request using the userId from local storage
      this.http.get<EtudiantConnect>(`http://localhost:8060/api/auth/etudiant/connect/${userId}`)
        .subscribe({
          next: (etudiantDto) => {
            this.studentData = etudiantDto;
            localStorage.setItem('idetudiant', this.studentData.id.toString());
            console.log("etudiant : "  + this.studentData);
            console.log("etudiant id : "  + localStorage.getItem('idetudiant'));
          },

          error: (error) => {
            console.error('Error fetching etudiant data', error);
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

  logout() {
    this.router.navigate(['/login']);
  }

  isActive(route: string): boolean {
    return this.router.url === route;
  }
}
