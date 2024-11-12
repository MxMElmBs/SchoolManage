import { Component, OnInit } from '@angular/core';
import { StudentsIHeaderComponent } from '../../StudentsIHeader/students-iheader/students-iheader.component';
import { StudentsIDashBComponent } from '../../StudentsIDashB/students-idash-b/students-idash-b.component';
import { EtudiantConnect } from '../../../models/EtudiantConnect';
import { Router } from '@angular/router';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';

@Component({
  selector: 'app-students-iacceuil',
  standalone: true,
  imports: [    StudentsIDashBComponent,
    StudentsIHeaderComponent],
  templateUrl: './students-iacceuil.component.html',
  styleUrl: './students-iacceuil.component.css'
})
export class StudentsIAcceuilComponent implements OnInit {
  idUser: number = 0;
  signature: string = '';
  etudiant: EtudiantConnect = new EtudiantConnect(0, '','','','','','','');

  constructor(private router: Router, private etudiantS: IEtudiantServicesService, private userco: LoginService) {}

  ngOnInit(): void {
    this.idUser = this.userco.getUserId();
    this.getEtudiant();
  }


  getEtudiant(): void {
    this.etudiantS.getConnectedEtudiant(this.idUser).subscribe(
      (data: EtudiantConnect) => {
        this.etudiant = data;// Logging the response for testing
        if (this.etudiant.sexe === 'M') {
          this.signature = 'M.';
        } else if (this.etudiant.sexe === 'F') {
          this.signature = 'Mme';
        } else {
          this.signature = ''; // Handle cases where sexe is not 'M' or 'F'
        }
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

  navigateTo(){
    this.router.navigate(['/app-etudiant-interface/studentsisetting']);
  }
}
