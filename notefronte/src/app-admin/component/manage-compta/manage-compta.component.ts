import { JwtInterceptor } from './../../../app-connexion/app/service/JwtInterceptor';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { OtherUser } from '../../model/OtherUser';
import { Utilisateur } from '../../model/Utilisateur';
import { SelectionModel } from '@angular/cdk/collections';
import { OtherUserService } from '../../services/secre/otheruser.service';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { UserService } from '../../services/user.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginService } from '../../../app-connexion/app/service/login.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-manage-compta',
  standalone: true,
  imports: [  CommonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    FormsModule,
    DashboardComponent,
    HeaderComponent,
    FooterComponent ],
  templateUrl: './manage-compta.component.html',
  styleUrl: './manage-compta.component.css',
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ]
})
export class ManageComptaComponent {
  ComptaCount: number = 0;
  userType: string = 'COMPTABLE';
  otherUsers: OtherUser[] = [];
  selectUser: OtherUser = new OtherUser('', '', '');
  selectedUser: Utilisateur = new Utilisateur();
  user: Utilisateur[] = [];
  displayedColumns: string[] = ['select', 'id', 'nom', 'prenom', 'email', 'username', 'password'];
  message: string | null = null;
  errorMessage: string = '';
  selection = new SelectionModel<Utilisateur>(true, []);

  constructor(private comptaService: OtherUserService,
    private userService : UserService,
    private authService : LoginService,
    private snackBar: MatSnackBar) {}

  token = this.authService.getToken();
  ngOnInit(): void {
    this.loadOtherUsers(this.userType);
    this.getComptaCount(this.userType);
  }

  loadOtherUsers(role: string) {
    this.userService.getUserByRole(role).subscribe(
      (data: Utilisateur[]) => {
        this.user = data;
        this.errorMessage = '';  // Réinitialiser le message d'erreur en cas de succès
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération des utilisateurs';
        console.error(error);
      }
    );
  }

  // Fonction pour appeler l'API d'ajout via POST
  createOtherUser() {
    this.userService.ajouterOtherUser(this.selectUser).subscribe(
      (data: OtherUser) => {
        this.selectUser = new OtherUser();
        this.message = 'Utilisateur ajouté avec succès';
        this.creerUtilisateurs();
      },
      (error) => {
        this.message = 'Erreur lors de l\'ajout de l\'utilisateur';
        console.error(error);
      }
    );
  }

  // Fonction pour appeler l'API de création d'utilisateurs via GET
  creerUtilisateurs() {
    this.userService.creerOtherUtilisateurs().subscribe(
      (data: string) => {
        this.message = data;  // Message renvoyé par l'API
      },
      (error) => {
        this.message = 'Erreur lors de la création des utilisateurs';
        console.error(error);
      }
    );
  }


  createUser(): void {
    this.createOtherUser();
    this.loadOtherUsers(this.userType);
   }


  updateUser(): void {
    this.comptaService.updateUser(this.selectedUser.idUser, this.selectedUser.username, this.selectedUser.password, "Comptable").subscribe(
      () => this.loadOtherUsers(this.userType),
      error => console.error(error)
    );
  }

  getComptaCount(role: string): void {
    this.userService.countByRole(role).subscribe(
      (data: number) => {
        this.ComptaCount = data;  // Stocker le nombre d'utilisateurs
        console.log(`Nombre d'utilisateurs pour le rôle ${role}: ${data}`);
      },
      (error) => {
        console.error('Erreur lors de la récupération du nombre d\'utilisateurs:', error);
      }
    );
  }

  sendUserPass(): void {
    if (this.selection.selected.length === 0) {
      this.snackBar.open('No users selected', 'Close', { duration: 3000 });
      return;
    }

    this.selection.selected.forEach(user => {
      // Envoi de l'email avec les identifiants
      this.userService.sendCredentials(user).subscribe(
        response => {
          console.log(user);
          console.log(`Email sent to ${user.email}: ${response}`);
          this.snackBar.open(`Email sent to ${user.email}`, 'Close', { duration: 3000 });

          // Mise à jour de l'utilisateur avec l'objet UserDto complet
          this.userService.updateUserPrint(user).subscribe(
            updateResponse => {
              console.log(`User status updated for ${user.username}: ${updateResponse}`);
              this.snackBar.open(`User status updated for ${user.username}`, 'Close', { duration: 3000 });
            }
          );
        },
        error => {
          console.error(`Failed to send email to ${user.email}:`, error);
          this.snackBar.open(`Failed to send email to ${user.email}`, 'Close', { duration: 5000 });
        }
      );
    });
  }



  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.user.length;
    return numSelected === numRows;
  }

  /** Whether at least one element is selected but not all rows. */
  isSomeSelected() {
    return this.selection.selected.length > 0 && !this.isAllSelected();
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
    } else {
      this.user.forEach(row => this.selection.select(row));
    }
  }

  /** Toggles the selection for a given row. */
  toggleRow(row: Utilisateur) {
    this.selection.toggle(row);
  }

}
