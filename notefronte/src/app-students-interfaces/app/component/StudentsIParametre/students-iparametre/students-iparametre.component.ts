import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { StudentsIDashBComponent } from '../../StudentsIDashB/students-idash-b/students-idash-b.component';
import { StudentsIHeaderComponent } from '../../StudentsIHeader/students-iheader/students-iheader.component';
import { EtudiantConnect } from '../../../models/EtudiantConnect';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';


@Component({
  selector: 'app-students-iparametre',
  standalone: true,
  imports: [FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
  StudentsIDashBComponent,
StudentsIHeaderComponent],
  templateUrl: './students-iparametre.component.html',
  styleUrl: './students-iparametre.component.css'
})
export class StudentsIParametreComponent {

   idUser: number = 0;

   hidePassword: boolean = true;
  // Objet contenant les informations de l'étudiant
  student : EtudiantConnect = new EtudiantConnect();
  etudiant : EtudiantConnect = new EtudiantConnect(0, '', '', '', '', '', '', '', '', '');


  constructor(private etudiantS: IEtudiantServicesService, private userco: LoginService) { }

  ngOnInit(): void {
    this.idUser = this.userco.getUserId();
    console.log('ID User:', this.idUser);
    this.getEtudiant();
    this.getGenderLabel();
    console.log('Student:', this.student);
   // this.password = this.userco.getUserPassword();

  }

  getEtudiant(): void {
    this.etudiantS.getConnectedEtudiant(this.idUser).subscribe(
      (data: EtudiantConnect) => {
        this.student = data;// Logging the response for testing
        console.log('Student:', this.student);
      },
      (error) => {
        console.error('Error fetching student data:', error);
      }
    );
  }

  getGenderLabel(): string {
    if (this.student.sexe === 'M') {
      return 'Homme';
    } else if (this.student.sexe === 'F') {
      return 'Femme';
    } else {
      return 'Inconnu'; // Fallback in case there's another value
    }
  }


}
