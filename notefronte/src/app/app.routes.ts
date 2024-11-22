import { PaieComponent } from './../app-armel/app/LOGICIEL/PAGES/PAIEMENT/paie/paie.component';
import { Routes } from '@angular/router';
import { LoginComponent } from '../app-connexion/app/login/login.component';
import { EtudiantdashComponent } from '../app-partie-gigi/app/etudiantdash/etudiantdash.component';
import { SalleComponent } from '../app-partie-gigi/app/salle/salle.component';
import { PresenceComponent } from '../app-partie-gigi/app/presence/presence.component';
import { UeFormComponent } from '../app-partie-gigi/app/ue-form/ue-form.component';
import { TextBookComponent } from '../app-partie-gigi/app/text-book/text-book.component';
import { CahierComponent } from '../app-partie-gigi/app/cahier/cahier.component';
import { VolumeHoraireComponent } from '../app-partie-gigi/app/volume-horaire/volume-horaire.component';
import { DetailComponent } from '../app-partie-gigi/app/detail/detail.component';
import { MatiereComponent } from '../app-partie-gigi/app/matiere/matiere.component';
import { PermissionComponent } from '../app-partie-gigi/app/permission/permission.component';
import { DetailpermComponent } from '../app-partie-gigi/app/detailperm/detailperm.component';
import { PermhistoriqueComponent } from '../app-partie-gigi/app/permhistorique/permhistorique.component';
import { TexbookhistoComponent } from '../app-partie-gigi/app/texbookhisto/texbookhisto.component';
import { AjoutfiliereComponent } from '../app-partie-gigi/app/ajoutfiliere/ajoutfiliere.component';
import { UserFormComponent } from '../app-partie-gigi/app/user-form/user-form.component';
import { AttendanceMarkingComponent } from '../app-partie-gigi/app/attendance-marking/attendance-marking.component';
import { AbsenceReportComponent } from '../app-partie-gigi/app/absence-report/absence-report.component';
import { ProfesseurComponent } from '../app-partie-gigi/app/professeur/professeur.component';
import { DashboardProfComponent } from '../app-partie-gigi/app/dashboard-prof/dashboard-prof.component';
import { PoppresenceComponent } from '../app-partie-gigi/app/poppresence/poppresence.component';
import { ParticipantsComponent } from '../app-partie-gigi/app/participants/participants.component';
import { PopupComponent } from '../app-partie-gigi/app/popup/popup.component';
import { StudentdashComponent } from '../app-partie-gigi/app/studentdash/studentdash.component';
import { StudenttabComponent } from '../app-partie-gigi/app/studenttab/studenttab.component';
import { PermissionformComponent } from '../app-partie-gigi/app/permissionform/permissionform.component';
import { TableaubordComponent } from './page/tableaubord/tableaubord.component';
import { UeComponent } from './page/ue/ue.component';
import { NoteComponent } from './page/note/note.component';
import { BulletinComponent } from './page/bulletin/bulletin.component';
import { ProfileComponent } from './page/profile/profile.component';
import { DashboardComponent } from '../app-partie-gigi/app/dashboard/dashboard.component';
import { EtudiantComponent } from '../app-partie-gigi/app/etudiant/etudiant.component';
import { EtudiantjeffComponent } from './page/etudiantjeff/etudiantjeff.component';
import { DashboardjeffComponent } from './page/dashboardjeff/dashboardjeff.component';
import { ControlComponent } from '../app-chahib/app/component/control/control.component';
import { FormulaireComponent } from '../app-chahib/app/component/formulaire/formulaire.component';
import { HomechahibComponent } from '../app-chahib/app/component/homechahib/homechahib.component';
import { ControlmoreComponent } from '../app-chahib/app/component/controlmore/controlmore.component';
import { StudentsIAcceuilComponent } from '../app-students-interfaces/app/component/StudentsIAcceuil/students-iacceuil/students-iacceuil.component';
import { StudentsINotesComponent } from '../app-students-interfaces/app/component/StudentsINotes/students-inotes/students-inotes.component';
import { StudentsIProgrammeComponent } from '../app-students-interfaces/app/component/StudentsIProgramme/students-iprogramme/students-iprogramme.component';
import { StudentsIParametreComponent } from '../app-students-interfaces/app/component/StudentsIParametre/students-iparametre/students-iparametre.component';
import { ManageComptaComponent } from '../app-admin/component/manage-compta/manage-compta.component';
import { ManageDeComponent } from '../app-admin/component/manage-de/manage-de.component';
import { ManageTeacherComponent } from '../app-admin/component/manage-teacher/manage-teacher.component';
import { ManageStudentComponent } from '../app-admin/component/manage-student/manage-student.component';
import { AcceuilComponent } from '../app-admin/component/acceuil/acceuil.component';
import { DocProfComponent } from '../app-chahib/app/component/doc-prof/doc-prof.component';
import { DeDocComponent } from '../app-chahib/app/component/de-doc/de-doc.component';
import { UpdateControlComponent } from '../app-chahib/app/component/update-control/update-control.component';
import { DefitechComponent } from '../app-armel/app/LOGICIEL/PAGES/IMPRESSION/defitech/defitech.component';
import { FicheComponent } from '../app-armel/app/LOGICIEL/PAGES/IMPRESSION/fiche/fiche.component';
import { PrincipaleComponent } from '../app-armel/app/LOGICIEL/PAGES/principale/principale.component';
import { AccueilComponent } from '../app-armel/app/LOGICIEL/PAGES/accueil/accueil.component';
import { AddComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/ADD/add/add.component';
import { DeclassementComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/ADD/declassement/declassement.component';
import { ParcourComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/parcour/parcour.component';
import { LicenceJourComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/LicenceJOUR/licence-jour/licence-jour.component';
import { L1Component } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/LicenceJOUR/l1/l1.component';
import { L2Component } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/LicenceJOUR/l2/l2.component';
import { L3Component } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/LicenceJOUR/l3/l3.component';
import { BtsComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/BTS/bts/bts.component';
import { Bts1Component } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/BTS/BTS1/bts1.component';
import { Bts2Component } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/BTS/BTS2/bts2.component';
import { LicenceSoirComponent } from '../app-armel/app/LOGICIEL/PAGES/ELEVES/PARCOURS/LicenceSOIR/licence-soir/licence-soir.component';
import { RappelComponent } from '../app-armel/app/LOGICIEL/PAGES/rappel/rappel.component';
import { ResumepresenceComponent } from '../app-partie-gigi/app/resumepresence/resumepresence.component';
import { ClassedetailsComponent } from '../app-partie-gigi/app/classedetails/classedetails.component';
import { ResumecahierComponent } from '../app-partie-gigi/app/resumecahier/resumecahier.component';
import { EmploidutempsComponent } from '../app-partie-gigi/app/emploidutemps/emploidutemps.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'admin-dashboard',
    children:[
      { path: 'home', component: AcceuilComponent },
      { path: 'etudiants', component: ManageStudentComponent },
      { path: 'profs', component: ManageTeacherComponent },
      { path: 'dse', component: ManageDeComponent },
      { path: 'comptables', component: ManageComptaComponent }
    ]
  },
  {
    path: 'app-gigi',
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        children: [
          { path: 'etudiantdash', component: EtudiantdashComponent },
          { path: '', redirectTo: 'etudiantdash', pathMatch: 'full' },
          { path: 'etudiant', component: EtudiantComponent },
          { path: 'salle', component: SalleComponent },
          { path: 'presence', component: PresenceComponent },
          { path: 'ue-form', component: UeFormComponent },
          { path: 'textBook', component: TextBookComponent },
          { path: 'cahier', component: CahierComponent },
          { path: 'volume-horaire', component: VolumeHoraireComponent },
          { path: 'detail/:id', component: DetailComponent },
          { path: 'matiere', component: MatiereComponent },
          { path: 'permission', component: PermissionComponent },
          { path: 'detailperm/:id', component: DetailpermComponent },
          { path: 'resumepresence/:id', component: ResumepresenceComponent },
          { path: 'classedetails/:id', component: ClassedetailsComponent },
          { path: 'resume/:id', component: ResumecahierComponent },
          { path: 'permhistorique', component: PermhistoriqueComponent },
          { path: 'textbookhisto', component: TexbookhistoComponent },
          { path: 'filiere', component: AjoutfiliereComponent },
        ],
      },
      { path: 'user-form', component: UserFormComponent },
      { path: 'attendance', component: AttendanceMarkingComponent },
      { path: 'absence', component: AbsenceReportComponent },

      {
        path: 'professeur',
        component: ProfesseurComponent,
        children: [
          { path: '', redirectTo: 'dashboard-prof', pathMatch: 'full' }, // Redirection
          { path: 'dashboard-prof', component: DashboardProfComponent },
          { path: 'poppresence', component: PoppresenceComponent },
          { path: 'salle', component: SalleComponent },
          { path: 'participants', component: ParticipantsComponent },
          { path: 'emploidutemps', component: EmploidutempsComponent },
        ],
      },
      { path: 'popup', component: PopupComponent },
      {
        path: 'studentdash',
        component: StudentdashComponent,
        children: [
          { path: 'textBook', component: TextBookComponent },
          { path: 'studenttab', component: StudenttabComponent },
          { path: 'permissionform', component: PermissionformComponent },
          { path: 'permhistorique', component: PermhistoriqueComponent },
        ],
      },
    ],
  },


  {
        path: 'app-jeff',
        children: [
          { path: '', component: DashboardjeffComponent, children: [
          { path: 'tableaubord', component: TableaubordComponent },
          { path: 'ue', component: UeComponent },
          { path: 'note', component: NoteComponent },
          { path: 'bulletin', component: BulletinComponent },
          { path: 'etudiantjeff', component: EtudiantjeffComponent },
          { path: 'profile', component: ProfileComponent }]}
        ]


    },

    {
      path: 'app-chahib',
      children: [
        { path: '', redirectTo: '/home', pathMatch: 'full' },
        { path: 'control', component: ControlComponent },
        { path: 'home', component:  HomechahibComponent },
        { path: 'doc-prof', component: DocProfComponent},
        { path: 'de-doc', component: DeDocComponent},
        { path: 'controlemore/:id', component: ControlmoreComponent },
        { path: 'update-control/:id', component: UpdateControlComponent },
        { path: 'formulaire/:id', component: FormulaireComponent },


      ]
  },

  {
    path: 'app-etudiant-interface',
    children:[
      { path: '', redirectTo: 'studentsidash', pathMatch: 'full' },
      { path: 'studentsidash', component: StudentsIAcceuilComponent },
      { path: 'studentsinotes', component: StudentsINotesComponent },
      { path: 'studentsiprog', component: StudentsIProgrammeComponent },
     // { path: 'studentsidepotdocs', component: },
      { path: 'studentsisetting', component: StudentsIParametreComponent },
    ]

  },

  {
    path: 'app-armel',
    children: [
      { path: 'defitech', component: DefitechComponent },

      { path: 'fiche-de-renvoi', component: FicheComponent },
      {

        path: '',
        component: PrincipaleComponent,
        children: [
          { path: 'statistique', component: AccueilComponent },
          { path: 'inscrire-étudiant', component: AddComponent },
          { path: 'modifier-étudiant/:matricule', component: DeclassementComponent },
          { path: 'parcours', component: ParcourComponent },
          { path: 'licence-du-jour' ,  component: LicenceJourComponent },
              { path: 'licence1', component: L1Component },
              { path: 'licence2', component: L2Component },
              { path: 'licence3', component: L3Component },
          { path: 'BTS', component: BtsComponent },
              { path: 'bts1', component: Bts1Component },
              { path: 'bts2', component: Bts2Component },
         { path: 'licence-du-soir', component: LicenceSoirComponent },
        /*  { path: 'scolarite', component: ScolariteComponent },*/
        { path: 'paiement/:matricule', component: PaieComponent },

          /*{ path: 'profil', component: ParametreComponent },*/
          { path: 'rappel', component: RappelComponent },
          { path: 'scolarite', component: PaieComponent },
        ]

      },
    ]
  },

];
