import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { JwtInterceptor } from './service/JwtInterceptor';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,   HttpClientModule
  ],  // Import du HttpClientModule ici pour utiliser l'intercepteur
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true  // Permet de gérer plusieurs intercepteurs
    }
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'connexion';
  constructor(private router: Router) {}
}
