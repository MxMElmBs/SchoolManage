import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { TeacherService } from '../../services/teacher/teacher.service';
import { SelectionModel } from '@angular/cdk/collections';
import { Utilisateur } from '../../model/Utilisateur';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-manage-teacher',
  standalone: true,
  imports: [CommonModule,
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
    FooterComponent],
  templateUrl: './manage-teacher.component.html',
  styleUrl: './manage-teacher.component.css'
})
export class ManageTeacherComponent implements OnInit{
  TeacherCount: number = 0;
  message: string | null = null;
  userType: string = 'PROFESSEUR';
  selectedUser: Utilisateur = new Utilisateur();
  user: Utilisateur[] = [];
  displayedColumns: string[] = ['select', 'id', 'nom', 'prenom', 'username', 'password'];
  selection = new SelectionModel<Utilisateur>(true, []);

  constructor(private teacherService: TeacherService, private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadOtherUsers(this.userType);
    this.getComptaCount(this.userType);
  }

  loadOtherUsers(role: string): void {
    this.userService.getUserByRole(role).subscribe(
      (data: Utilisateur[]) => {
        this.user = data;
        this.message= '';  // Réinitialiser le message d'erreur en cas de succès
      },
      (error) => {
        this.message = 'Erreur lors de la récupération des utilisateurs';
        console.error(error);
      }
    );
  }



  createUser(): void {
    this.creerComptesUserTeacher(); // Créer l'utilisateur et ensuite le compte utilisateur pour le secrétaire
  }

  creerComptesUserTeacher(): void {
    this.userService.creerUtilisateursProf().subscribe(
      (data: string) => {
        this.message = data;  // Message renvoyé par l'API
        this.ngOnInit;
      },
      (error) => {
        this.message = 'Erreur lors de la création des utilisateurs';
        console.error(error);
      }
    );
  }


  updateUser(): void {
    this.teacherService.updateUser(this.selectedUser.idUser, this.selectedUser.username, this.selectedUser.password, "Professeur").subscribe(
      () => this.loadOtherUsers(this.userType),
      error => console.error(error)
    );
  }

  getComptaCount(role: string): void {
    this.userService.countByRole(role).subscribe(
      (data: number) => {
        this.TeacherCount = data;  // Stocker le nombre d'utilisateurs
        console.log(`Nombre d'utilisateurs pour le rôle ${role}: ${data}`);
      },
      (error) => {
        console.error('Erreur lors de la récupération du nombre d\'utilisateurs:', error);
      }
    );
  }

  /*deleteUser(): void {
    const selectedUsers = this.selection.selected.map(user => user.idUser);

    if (selectedUsers.length === 0) {
      console.warn("No users selected for deletion.");
      return;
    }

    selectedUsers.forEach(id => {
      this.teacherService.dele(id).subscribe(
        () => this.ngOnInit(), // Recharger les utilisateurs après chaque suppression
        error => console.error(`Error deleting user with ID ${id}:`, error)
      );
    });
    // Clear the selection after deletion
    this.selection.clear();
  }*/

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
