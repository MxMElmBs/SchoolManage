import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student/student.service';
import { Utilisateur } from '../../model/Utilisateur';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { UserService } from '../../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-manage-student',
  standalone: true,
  imports: [
    CommonModule,
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
    FooterComponent
  ],
  templateUrl: './manage-student.component.html',
  styleUrls: ['./manage-student.component.css']
})

export class ManageStudentComponent {
  filieres = ['Filière 1', 'Filière 2', 'Filière 3'];
  userType: string = 'ETUDIANT';
  StudentCount: number = 0;
  errorMessage: string = '';
  message: string | null = null;
  selectedUser: Utilisateur = new Utilisateur();
  user: Utilisateur[] = [];
  user1 = new MatTableDataSource<Utilisateur>(this.user);
  displayedColumns: string[] = ['select', 'id', 'nom', 'prenom', 'email', 'username', 'password'];
  selection = new SelectionModel<Utilisateur>(true, []);

  constructor(private studyService: StudentService, private userService: UserService,
    private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadOtherUsers(this.userType);
    this.getComptaCount(this.userType);
  }

  loadOtherUsers(role: string) {
    this.userService.getUserByRole(role).subscribe(
      (data: Utilisateur[]) => {
        this.user = data; // Store the data in the user array
        this.user1.data = this.user;   // Réinitialiser le message d'erreur en cas de succès
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération des utilisateurs';
        console.error(error);
      }
    );
  }

  creerComptesUserTeacher(): void {
    this.userService.creerUtilisateursPourEtudiants().subscribe(
      response => {
        this.message = response;
        this.ngOnInit();
      },
      error => {
        console.error('Erreur lors de la création des utilisateurs', error);
        this.message = 'Erreur lors de la création des utilisateurs';
      }
    );
  }

  createUser(): void {
    this.creerComptesUserTeacher(); // Créer l'utilisateur et ensuite le compte utilisateur pour le secrétaire
  }


  updateUser(): void {
    this.studyService.updateUser(this.selectedUser.idUser, this.selectedUser.username, this.selectedUser.password, "Etudiant").subscribe(
      () => this.loadOtherUsers(this.userType),
      error => console.error(error)
    );
  }

  getComptaCount(role: string): void {
    this.userService.countByRole(role).subscribe(
      (data: number) => {
        this.StudentCount = data;  // Stocker le nombre d'utilisateurs
        console.log(`Nombre d'utilisateurs pour le rôle ${role}: ${data}`);
      },
      (error) => {
        console.error('Erreur lors de la récupération du nombre d\'utilisateurs:', error);
      }
    );
  }

 /* deleteUser(): void {
    const selectedUsers = this.selection.selected.map(user => user.idUser);

    if (selectedUsers.length === 0) {
      console.warn("No users selected for deletion.");
      return;
    }

    selectedUsers.forEach(id => {
      this.studyService.dele(id).subscribe(
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

  applyFilter(event: Event) {
    const target = event.target as HTMLInputElement;
    const filterValue = target?.value ? target.value.trim().toLowerCase() : '';
    this.user1.filter = filterValue;
  }


}
