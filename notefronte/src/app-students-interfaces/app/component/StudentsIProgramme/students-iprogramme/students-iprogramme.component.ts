import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { StudentsIHeaderComponent } from '../../StudentsIHeader/students-iheader/students-iheader.component';
import { StudentsIDashBComponent } from '../../StudentsIDashB/students-idash-b/students-idash-b.component';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
import { EtudiantCours } from '../../../models/EtudiantCours';
import { TypeSemestre } from '../../../models/TypeSemestre.enum';
import { EtudiantConnect } from '../../../models/EtudiantConnect';

export interface CourseCard {
  filiere: string;
  niveau: string;
  semestre: string;
  name: string;
  code: string;
  credit: string;
  coef: number;
  color: string;
  description: string;
}

@Component({
  selector: 'app-students-iprogramme',
  standalone: true,
  imports: [CommonModule, MatIconModule,
    MatSelectModule, FormsModule,
    StudentsIDashBComponent,
StudentsIHeaderComponent],
  templateUrl: './students-iprogramme.component.html',
  styleUrl: './students-iprogramme.component.css'

})



export class StudentsIProgrammeComponent implements OnInit {

  idUser: number = 0;
  student : EtudiantConnect = new EtudiantConnect(0, '', '', '', '', '', '', '', '', '');
  selectedSemester!: TypeSemestre;
  semesters: TypeSemestre[] = [TypeSemestre.SEMESTRE_1, TypeSemestre.SEMESTRE_2, TypeSemestre.SEMESTRE_3, TypeSemestre.SEMESTRE_4,
    TypeSemestre.SEMESTRE_5, TypeSemestre.SEMESTRE_6];

  etudiantCours: EtudiantCours[] = [];
  cards: CourseCard[] = []; // Properly typed cards array
  showModal: boolean = false; // Control modal visibility
  selectedCourse: CourseCard | null = null; // Store the selected course

  constructor(
    private authService: LoginService,
    private etudiantCourService: IEtudiantServicesService
  ){}

  ngOnInit(): void {
    this.idUser = this.authService.getUserId();
    console.log('ID de l\'utilisateur connecté :', this.idUser);

    this.getEtudiant(() => {
      // Appeler les cours seulement après avoir obtenu les informations sur l'étudiant
      this.getCoursByIdUser();
    });
  }


  getEtudiant(callback: () => void): void {
    this.etudiantCourService.getConnectedEtudiant(this.idUser).subscribe(
      (data: EtudiantConnect) => {
        this.student = data;
        console.log('Données de l\'étudiant:', this.student);
        if (callback) callback(); // Exécutez le callback après avoir récupéré l'étudiant
      },
      (error) => {
        console.error('Erreur lors de la récupération des données de l\'étudiant:', error);
      }
    );
  }


  // Fetch courses based on user and semester
  getCoursByIdUserAndSemestre(): void {
    console.log('Donnée student : ', this.student)
    console.log('Semestre sélectionné:', this.selectedSemester);
    this.etudiantCourService.getCoursByIdUserAndSemestre(this.idUser, this.selectedSemester).subscribe(
      (data: EtudiantCours[]) => {
        this.etudiantCours = data; // Correct data assignment
        this.mapCoursesToCards(data);
      },
      (error) => {
        console.error('Erreur lors de la récupération des cours:', error);
      }
    );
  }

  // Fetch courses based on user without semester
  getCoursByIdUser(): void {
    this.etudiantCourService.getCoursByIdUser(this.idUser).subscribe(
      (data: EtudiantCours[]) => {
        this.etudiantCours = data; // Correct data assignment
        this.mapCoursesToCards(data);
      },
      (error) => {
        console.error('Erreur lors de la récupération des cours:', error);
      }
    );
  }

    // Maps EtudiantCours data to cards for UI
  private mapCoursesToCards(data: EtudiantCours[]): void {
    this.cards = data.map(course => ({
      filiere: course.nomFiliere,
      niveau: course.niveau,
      semestre: course.semestre,
      name: course.libelleUE,
      code: course.code,
      credit: "Crédit ou Coef", // Static or dynamic value
      coef: course.creditUE, // Coefficient or credit value
      color: this.getColorByTypeUe(course.typeUe), // Dynamic color based on UE type
      description: course.description
    }));
  }


  // Returns a color based on the UE type
  getColorByTypeUe(type: string): string {
    switch (type) {
      case 'Spécialité':
        return '#FF6347'; // Tomato
      case 'Fondamentale':
        return '#4682B4'; // SteelBlue
      case 'Transversale':
        return '#32CD32'; // LimeGreen
      case 'Complémentaire':
        return '#FF8C00'; // BlackOrange
      case 'Libre':
        return '#800080'; // Purple
      default:
        return '#0029FF'; // Default blue
    }
  }

  // Method to open the modal and display the course details
openCourseDetails(card: CourseCard): void {
  this.selectedCourse = card; // Now selectedCourse should also be typed as CourseCard or null
  this.showModal = true;
}

  // Method to close the modal
  closeModal(): void {
    this.showModal = false;
  }

}
