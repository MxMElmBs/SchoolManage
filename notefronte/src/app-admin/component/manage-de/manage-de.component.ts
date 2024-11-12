import { UserService } from './../../services/user.service';
import { DeService } from './../../services/de/de.service';
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
import { DirecEtude } from '../../model/DirecEtude';
import { Utilisateur } from '../../model/Utilisateur';
import { SelectionModel } from '@angular/cdk/collections';
import { MatDialog } from '@angular/material/dialog';
import { EditComponent } from '../popup/edit/edit/edit.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Parcours } from '../../model/Parcours';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-manage-de',
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
    EditComponent,
  DashboardComponent,
HeaderComponent,
FooterComponent],
  templateUrl: './manage-de.component.html',
  styleUrl: './manage-de.component.css'
})
export class ManageDeComponent implements OnInit {
  DeCount: number = 0;
  de: DirecEtude[] = [];
  userType: string = 'DE';
  message: string | null = null;
  selectUser: DirecEtude = new DirecEtude('', '', '', '');
  selectedUser: Utilisateur = new Utilisateur();
  user: Utilisateur[] = [];
  displayedColumns: string[] = ['select', 'id', 'nom', 'prenom', 'email', 'username', 'password'];
  selection = new SelectionModel<Utilisateur>(true, []);
  parcoursList: Parcours[] = [];  // Stocker la liste des parcours récupérés

  constructor(private deService: DeService,  private dialog: MatDialog,
               private userService: UserService ,
               private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadDEUser(this.userType);
    this.getSecretaireCount(this.userType);
    // Appeler le service pour récupérer la liste des parcours lors du chargement du composant
    this.userService.getParcours().subscribe(
      (data: Parcours[]) => {
        this.parcoursList = data;  // Stocker les parcours récupérés
        console.log('Parcours récupérés:', data);
      },
      (error) => {
        console.error('Erreur lors de la récupération des parcours:', error);
      }
    );
  }

  openEditDialog(): void {
  /*  if (this.selection.selected.length === 0) {
      console.error('No user selected for editing');
      return;
    }

    const selectedUser = this.selection.selected[0];

    const dialogRef = this.dialog.open(EditComponent, {
      width: '80px',
      data: { user: { ...selectedUser } }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateUser(result);
      }
    });*/
  }

  /*updateUser(updatedUser: Utilisateur): void {
    this.deService.updateUserDE(updatedUser).subscribe(
      () => this.loadDEUser(),
      error => console.error(error)
    );
  }*/

  loadDEUser(role: string): void {
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

  createOtherUser(): void {
    this.userService.ajouterDEUser(this.selectUser).subscribe(
      (data: DirecEtude) => {
        this.selectUser = new DirecEtude();
        this.message = 'Utilisateur ajouté avec succès';
        this.creerUtilisateurs();
      },
      (error) => {
        this.message = 'Erreur lors de l\'ajout de l\'utilisateur';
        console.error(error);
      }
    );
  }

  creerUtilisateurs(): void {
    this.userService.creerDEUtilisateurs().subscribe(
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
    this.loadDEUser(this.userType);
    // Créer l'utilisateur et ensuite le compte utilisateur pour le secrétaire
  }

  getSecretaireCount(role: string): void {
    this.userService.countByRole(role).subscribe(
      (data: number) => {
        this.DeCount = data;  // Stocker le nombre d'utilisateurs
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
