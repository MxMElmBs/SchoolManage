import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterOutlet } from '@angular/router';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';
import { EtudiantConnect } from '../../../models/EtudiantConnect';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
@Component({
  selector: 'app-students-idash-b',
  standalone: true,
  imports: [MatIconModule, CommonModule, RouterOutlet],
  templateUrl: './students-idash-b.component.html',
  styleUrl: './students-idash-b.component.css'
})
export class StudentsIDashBComponent {
  menuItems = [
    { name: 'Acceuil', icon: 'dashboard' },
    { name: 'Notes', icon: 'school' },
    { name: 'Programme d\'étude', icon: 'person' },
    { name: 'Gestion cahier de texte', icon: 'event_note'},
    { name: "Dépôts de document \n (Tuteuré et Mémoire)" , icon: 'book' },
    { name: 'Profils', icon: 'settings'  }
  ];


  student : EtudiantConnect = new EtudiantConnect(0, '', '', '', '', '', '', '', '', '');
  idUser: number = 0;

  constructor(private router: Router, private userCo: LoginService, private etudiantS: IEtudiantServicesService) {}

  ngOnInit(): void {
    this.idUser = this.userCo.getUserId();
   // this.password = this.userco.getUserPassword();
   this.getEtudiant()
   console.log('ID User:', this.idUser);
   console.log('Student:', this.student);
  }

  getEtudiant(): void {
    this.etudiantS.getConnectedEtudiant(this.idUser).subscribe(
      (data: EtudiantConnect) => {
        this.student = data;// Logging the response for testing
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

  navigateTo(name: string): void {
    if (name === 'Acceuil') {
      this.router.navigate(['/app-etudiant-interface/studentsidash']);
    }
    if (name === 'Notes') {
      this.router.navigate(['/app-etudiant-interface/studentsinotes']);
    }
    if (name === 'Programme d\'étude') {
      this.router.navigate(['/app-etudiant-interface/studentsiprog']);
    }
    if (name === 'Gestion cahier de texte') {
      this.router.navigate(['/app-gigi/studentdash/textBook']);
    }
    if (name === "Dépôts de document \n (Tuteuré et Mémoire)") {
      if (
        (this.student.parcours === 'BTS' && this.student.niveau === 'DEUXIEME_ANNEE') ||
        (this.student.parcours === 'Licence du jour' && this.student.niveau === 'TROISIEME_ANNEE') ||
        (this.student.parcours === 'Licence du soir' && this.student.niveau === 'TROISIEME_ANNEE')
      ) {
        this.router.navigate(['/app-chahib/home']);
      }
      else {
        // Afficher un message d'erreur si l'étudiant ne remplit pas les conditions
        alert('Vous n\'avez pas le niveau requis pour accéder à cette plateforme.');
      }
    }
    if (name === 'Profils') {
      this.router.navigate(['/app-etudiant-interface/studentsisetting']);
    }
    }



}
