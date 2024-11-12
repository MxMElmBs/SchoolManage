import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { EtudiantdashComponent } from './etudiantdash/etudiantdash.component';
import { EtudiantComponent } from './etudiant/etudiant.component';
import { SalleComponent } from './salle/salle.component';
import { PresenceComponent } from './presence/presence.component';
import { UserFormComponent } from './user-form/user-form.component';
import { UeFormComponent } from './ue-form/ue-form.component';
import { TextBookComponent } from './text-book/text-book.component';
import { CahierComponent } from './cahier/cahier.component';
import { VolumeHoraireComponent } from './volume-horaire/volume-horaire.component';
import { AttendanceMarkingComponent } from './attendance-marking/attendance-marking.component';
import { AbsenceReportComponent } from './absence-report/absence-report.component';
import { DashboardProfComponent } from './dashboard-prof/dashboard-prof.component';
import { ListecoursComponent } from './listecours/listecours.component';
import { PopupComponent } from './popup/popup.component';
import { ParticipantsComponent } from './participants/participants.component';
import { PoppresenceComponent } from './poppresence/poppresence.component';
import { StudentdashComponent } from './studentdash/studentdash.component';
import { StudenttabComponent } from './studenttab/studenttab.component';
import { ProfesseurComponent } from './professeur/professeur.component';
import { DetailComponent } from './detail/detail.component';
import { MatiereComponent } from './matiere/matiere.component';
import { PermissionComponent } from './permission/permission.component';
import { DetailpermComponent } from './detailperm/detailperm.component';
import { PermissionformComponent } from './permissionform/permissionform.component';
import { PermhistoriqueComponent } from './permhistorique/permhistorique.component';
import { TexbookhistoComponent } from './texbookhisto/texbookhisto.component';

export const routes: Routes = [
   /* { path: 'dashboard', component: DashboardComponent ,children: [
        { path: '', redirectTo: 'etudiantdash', pathMatch: 'full' },
        { path: 'etudiantdash', component: EtudiantdashComponent },
        { path: 'etudiant', component: EtudiantComponent},
        {path : 'salle', component: SalleComponent},
        {path: 'presence', component: PresenceComponent},
        { path:'ue-form', component: UeFormComponent},
        { path: 'textBook', component: TextBookComponent},
        { path: 'cahier', component : CahierComponent},
        { path: 'volume-horaire', component : VolumeHoraireComponent},
        { path: 'detail', component: DetailComponent},
        { path: 'matiere', component: MatiereComponent},
        { path:'permission', component: PermissionComponent},
        { path: 'detailperm', component: DetailpermComponent},
        { path: 'permhistorique', component: PermhistoriqueComponent},
        { path: 'textbookhisto', component: TexbookhistoComponent},
    ]
    } ,
    { path: 'user-form', component: UserFormComponent},
    { path: 'attendance', component: AttendanceMarkingComponent},
    { path: 'absence', component: AbsenceReportComponent},


    {
        path: 'professeur',
        component: ProfesseurComponent,
        children: [
            { path: '', redirectTo: 'dashboard-prof', pathMatch: 'full' }, // Redirection
            { path: 'dashboard-prof', component: DashboardProfComponent }  ,
            { path: 'poppresence', component: PoppresenceComponent},
            {path : 'salle', component: SalleComponent},
            { path: 'participants', component: ParticipantsComponent},

        ]  },

    { path: 'popup', component: PopupComponent},

    {path: 'studentdash' , component: StudentdashComponent , children :[
        { path: 'textBook', component: TextBookComponent},
        { path: 'studenttab', component: StudenttabComponent},
        { path: 'permissionform', component: PermissionformComponent},
        { path: 'permhistorique', component: PermhistoriqueComponent},
    ]},*/

];
