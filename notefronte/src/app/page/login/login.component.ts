import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet,  } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/Auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, RouterOutlet, CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }

    if (this.authService.login(this.username, this.password)) {
      this.router.navigate(['/tableaubord']); // Redirection vers le tableau de bord si connexion réussie
    } else {
      this.errorMessage = 'Identifiant ou mot de passe incorrect.';
      setTimeout(() => {
        this.errorMessage = '';
      }, 5000);
    }
  }

}
