import { Routes } from '@angular/router';
import { StudentsINotesComponent } from './component/StudentsINotes/students-inotes/students-inotes.component';
import { StudentsIProgrammeComponent } from './component/StudentsIProgramme/students-iprogramme/students-iprogramme.component';
import { StudentsIAcceuilComponent } from './component/StudentsIAcceuil/students-iacceuil/students-iacceuil.component';
import { StudentsIParametreComponent } from './component/StudentsIParametre/students-iparametre/students-iparametre.component';

export const routes: Routes = [
  { path: '', redirectTo: 'studentsidash', pathMatch: 'full' },
  { path: 'studentsidash', component: StudentsIAcceuilComponent },
  { path: 'studentsinotes', component: StudentsINotesComponent },
  { path: 'studentsiprog', component: StudentsIProgrammeComponent },
 // { path: 'studentsidepotdocs', component: StudentsI},
  { path: 'studentsisetting', component: StudentsIParametreComponent },




];
