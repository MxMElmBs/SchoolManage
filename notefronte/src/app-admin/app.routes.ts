import { ManageStudentComponent } from './component/manage-student/manage-student.component';
import { Routes } from '@angular/router';
import { AcceuilComponent } from './component/acceuil/acceuil.component';
import { ManageTeacherComponent } from './component/manage-teacher/manage-teacher.component';
import { ManageDeComponent } from './component/manage-de/manage-de.component';
import { ManageComptaComponent } from './component/manage-compta/manage-compta.component';
import { ParametreComponent } from './component/parametre/parametre.component';
import { ManageSecreComponent } from './component/manage-secre/manage-secre.component';


export const routes: Routes = [
  { path: '', redirectTo: '/connexion', pathMatch: 'full' },
  { path: 'home', component: AcceuilComponent },
  { path: 'etudiants', component: ManageStudentComponent },
  { path: 'profs', component: ManageTeacherComponent },
  { path: 'secretaire', component: ManageSecreComponent },
  { path: 'dse', component: ManageDeComponent },
  { path: 'comptables', component: ManageComptaComponent },
  { path: 'setting', component: ParametreComponent }
];

