import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'; // Importer les interceptors
import { JwtInterceptor } from '../service/JwtInterceptor'; // Votre intercepteur JWT
import { AuthResponse } from '../service/authResponse';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule], // Ajouter HttpClientModule
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true } // Configurer l'intercepteur ici
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  rememberMe: boolean = false;
  selectedRole: string = '';

  constructor(private authService: LoginService, private router: Router) { }

  // Choisir le rôle de l'utilisateur
  selectRole(role: string) {
    this.selectedRole = role;
    console.log(`Selected role: ${this.selectedRole}`);
  }

  // Envoyer les informations de connexion
  onSubmit() {
    if (this.username && this.password && this.selectedRole) {
      this.authService.login(this.username, this.password, this.selectedRole).subscribe(
        (response: AuthResponse) => {
          console.log('Réponse de l\'API :', response);

          if (response.jwt) {
            // Enregistrer le token JWT
            this.authService.saveToken(response.jwt);
          }

          if (response.userId) {
            // Enregistrer les informations utilisateur
            this.authService.saveUserInfo({
              userId: response.userId,  // Remplacer idUser par userId
              username: response.username,
              role: this.selectedRole
            });

            // Rediriger selon le rôle de l'utilisateur
            this.redirectBasedOnRole(this.selectedRole);
            console.log(this.authService.getUserInfo())
            console.log('Connexion réussie id= ', this.authService.getUserId());
          } else {
            console.error('Les informations de l\'utilisateur ne sont pas présentes dans la réponse.');
          }
        },
        (error) => {
          console.error('Échec de la connexion:', error);
          alert('Nom d\'utilisateur, mot de passe ou rôle invalide');
        }
      );
    } else {
      alert('Veuillez remplir tous les champs et sélectionner un rôle.');
    }
  }

  // Rediriger en fonction du rôle sélectionné
  redirectBasedOnRole(role: string) {
    switch (role) {
      case 'ADMIN':
        this.router.navigate(['/admin-dashboard/home']);
        break;
      case 'PROFESSEUR':
        this.router.navigate(['/app-gigi/professeur']);
        break;
      case 'ETUDIANT':
        this.router.navigate(['/app-etudiant-interface/studentsidash']);
        break;
      case 'DE':
        this.router.navigate(['/app-gigi/dashboard']);
        break;
      case 'COMPTABLE':
        this.router.navigate(['/app-armel/statistique']);
        break;
      default:
        console.error('Rôle non reconnu:', role);
        alert('Rôle non reconnu');
    }
  }
}
