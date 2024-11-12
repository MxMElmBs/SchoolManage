import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./component/header/header.component";
import { DashboardComponent } from "./component/dashboard/dashboard.component";
import { FooterComponent } from "./component/footer/footer.component";
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { JwtInterceptor } from '../app-connexion/app/service/JwtInterceptor';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, DashboardComponent, FooterComponent,
      HttpClientModule
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
  title = 'projet-soutenance';
}
