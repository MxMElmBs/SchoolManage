import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';
import { EtudiantConnect } from '../../../models/EtudiantConnect';
import { JwtInterceptor } from '../../../../../app-connexion/app/service/JwtInterceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

@Component({
  selector: 'app-students-iheader',
  standalone: true,
  imports: [MatIcon, ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  templateUrl: './students-iheader.component.html',
  styleUrl: './students-iheader.component.css'
})
export class StudentsIHeaderComponent {

  idUser: number = 0;
  etudiant: EtudiantConnect = new EtudiantConnect(0, '','','','','','','');

  constructor(private router: Router, private etudiantS: IEtudiantServicesService, private userco: LoginService) {}

  ngOnInit(): void {
    this.idUser = this.userco.getUserId();
    this.getEtudiant();
  }


  logout(): void {
    this.userco.logout();
    this.router.navigate(['/login']);
    // Implémentez la logique de déconnexion ici
    console.log('Déconnexion');
  }

  getEtudiant(): void {
    this.etudiantS.getConnectedEtudiant(this.idUser).subscribe(
      (data: EtudiantConnect) => {
        this.etudiant = data;// Logging the response for testing
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

}
