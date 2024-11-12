import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { StudentsIHeaderComponent } from '../../StudentsIHeader/students-iheader/students-iheader.component';
import { StudentsIDashBComponent } from '../../StudentsIDashB/students-idash-b/students-idash-b.component';
import { IEtudiantServicesService } from '../../../services/ietudiant-services.service';
import { EtudiantNoteMaxDto } from '../../../models/EtudiantNoteMaxDto';
import { LoginService } from '../../../../../app-connexion/app/service/login.service';
import { TypeSemestre } from '../../../models/TypeSemestre.enum';


@Component({
  selector: 'app-students-inotes',
  standalone: true,
  imports: [CommonModule, MatIconModule,
    MatSelectModule, FormsModule, MatTableModule,
    StudentsIDashBComponent,
    StudentsIHeaderComponent],
  templateUrl: './students-inotes.component.html',
  styleUrl: './students-inotes.component.css'
})
export class StudentsINotesComponent {

  semesters : TypeSemestre[] = [TypeSemestre.SEMESTRE_1,
    TypeSemestre.SEMESTRE_2, TypeSemestre.SEMESTRE_3,
    TypeSemestre.SEMESTRE_4, TypeSemestre.SEMESTRE_5,
    TypeSemestre.SEMESTRE_6];
  notes: EtudiantNoteMaxDto[] = [];
  selectedSemester : string = '';
  idUser: number = 0;
  courses: any[] = [];
  displayedColumns: string[] = ['label', 'grade']; // Columns to display in the table


  constructor(private etudianService: IEtudiantServicesService, private userco: LoginService){}
  ngOnInit(): void {
    this.idUser = this.userco.getUserId();
    console.log('ID de l\'utilisateur connecté :', this.idUser);
    this.getNotesOfStudents();
  }

  getNotesOfStudents(): void {
    this.etudianService.getNotesByEtudiantId(this.idUser).subscribe(
      (data: EtudiantNoteMaxDto[]) => {
        console.log('Notes reçues : ', data);
        this.notes = data;
        this.formatCourses();
      },
      error => {
        console.error('Error fetching notes', error);
      }
    );
  }


  formatCourses(): void {
    console.log('Notes avant formatage : ', this.notes); // Ajoutez ce log

    const coursesMap: { [key: string]: any } = {};

    this.notes.forEach(note => {
      console.log('Traitement de la note : ', note); // Log de chaque note

      // Assurez-vous que les propriétés existent
      console.log('Devoir: ', note.devoir, 'Examen: ', note.examen);

      const courseKey = note.ueCode + note.semestre; // Clé unique par cours et semestre
      const devoirNote = note.devoir;
      const examenNote = note.examen;

      if (!coursesMap[courseKey]) {
        coursesMap[courseKey] = {
          title: note.ueLibelle,
          code: note.ueCode,
          typeUe: note.typeUe,
          grades: [{ label: 'Devoir', grade: devoirNote + '/20' },
                    { label: 'Examen', grade: examenNote + '/20' }],
          average: '',
          semester: note.semestre,
        };
      }
      // Calculer la moyenne
      coursesMap[courseKey].average = (devoirNote*40/100 + examenNote*60/100).toFixed(2) + '/20';
    });

    // Convertir la carte en tableau
    this.courses = Object.values(coursesMap);
  }


}
