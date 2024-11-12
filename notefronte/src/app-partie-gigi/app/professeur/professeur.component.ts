import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgOptimizedImage } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../../app-connexion/app/service/login.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-professeur',
  standalone: true,
  imports: [RouterOutlet, CommonModule, MatSidenavModule, RouterLink, MatSnackBarModule, NgOptimizedImage,
     MatToolbarModule, MatIconModule],
  templateUrl: './professeur.component.html',
  styleUrls: ['./professeur.component.css'] // Fixed typo from styleUrl to styleUrls
})
export class ProfesseurComponent implements OnInit {
  data: any | null = null;
  idUser: number = 0;

  constructor(private router: Router, private http: HttpClient, private authCo : LoginService) {}

  ngOnInit() {

    this.idUser= this.authCo.getUserId();
    localStorage.setItem('userId', this.idUser.toString());
    console.log("userId" +localStorage.getItem('userId'));

    const userId = localStorage.getItem('userId');
    console.log("userId" +userId)
    if (userId) {
      this.http.get<any>(`http://localhost:8060/api/auth/professeur/professeurInfo/${userId}`)
        .subscribe({
          next: (professeurDto) => {
            this.data = professeurDto;
            console.log('Professeur data: ', professeurDto);
            localStorage.setItem('professeurId', professeurDto.professeurId.toString());
          },
          error: (error) => {
            console.error('Error fetching professeur data', error);
          }
        });
    } else {
      console.error('No userId found in local storage');
    }
  }
  logout() {
    this.router.navigate(['/login']);
  }

  get content() {
    return this.data ? `${this.data.nom} ${this.data.prenom}` : 'Loading...';
  }
}
