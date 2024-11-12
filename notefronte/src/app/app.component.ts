import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DashboardjeffComponent } from './page/dashboardjeff/dashboardjeff.component';
import { TableaubordComponent } from './page/tableaubord/tableaubord.component';
import { HeaderComponent } from './page/header/header.component';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { JwtInterceptor } from '../app-connexion/app/service/JwtInterceptor';
import { ReactiveFormsModule } from '@angular/forms';
// import { TableaubordComponent } from './page/tableaubord/tableaubord.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,
    DashboardjeffComponent,
    HeaderComponent,
    ReactiveFormsModule,
    TableaubordComponent,
    CommonModule,
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
  title = 'notefronte';

}
