import { Utilisateur } from './../../model/Utilisateur';
import { OtherUser } from './../../model/OtherUser';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { OtherUserService } from '../../services/secre/otheruser.service';
import { SelectionModel } from '@angular/cdk/collections';
import { MatCheckboxModule } from '@angular/material/checkbox';


@Component({
  selector: 'app-manage-secre',
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
    FormsModule
  ],
  providers: [OtherUserService],
  templateUrl: './manage-secre.component.html',
  styleUrls: ['./manage-secre.component.css']
})
export class ManageSecreComponent implements OnInit {
  secretaireCount: number = 0;
  otherUsers: OtherUser[] = [];
  selectUser: OtherUser = new OtherUser(0, '', '', '', '');
  selectedUser: Utilisateur = new Utilisateur();
  user: Utilisateur[] = [];
  displayedColumns: string[] = ['select', 'id', 'nom', 'prenom', 'username', 'password'];
  selection = new SelectionModel<Utilisateur>(true, []);

  constructor(private secreService: OtherUserService) {}

  ngOnInit(): void {
    this.loadOtherUsers();
    this.getSecretaireCount();
  }

  loadOtherUsers(): void {
    this.secreService.getUserDetailsBySecretaire().subscribe(
      data => this.user = data,
      error => console.error(error)
    );
  }

  createOtherUser(): void {
    console.log(this.selectUser); // Vérifiez le contenu de l'objet
    this.secreService.createOtherUser(this.selectUser).subscribe(
      () => {
        this.selectUser = new OtherUser(); // Réinitialisation de l'objet après la création
        this.creerComptesUserSecretaire(); // Appel de la fonction pour créer le compte après la création de l'utilisateur
      },
      error => console.error(error)
    );
  }

  creerComptesUserSecretaire(): void {
    this.secreService.creerComptesUserSecretaire().subscribe(
      () => this.ngOnInit(), // Chargement des utilisateurs après la création du compte
      error => console.error(error)
    );
  }

  createUser(): void {
    this.createOtherUser(); // Créer l'utilisateur et ensuite le compte utilisateur pour le secrétaire
  }


  updateUser(): void {
    this.secreService.updateUser(this.selectedUser.idUser, this.selectedUser.username, this.selectedUser.password, "Secretaire").subscribe(
      () => this.loadOtherUsers(),
      error => console.error(error)
    );
  }

  getSecretaireCount(): void {
    this.secreService.countByRole('Secretaire').subscribe(
      (count: number) => {
        this.secretaireCount = count;
      },
      error => {
        console.error('Error fetching Secretaire count:', error);
      }
    );
  }

  sendUserPass(): void {
    if (this.selection.selected.length === 0) {
      console.error('No users selected');
      return;
    }

    this.selection.selected.forEach(user => {
      this.secreService.sendCredentials(user.email, user.username, user.password).subscribe(
        response => {
          console.log(`Email sent to ${user.email}: ${response}`);
        },
        error => {
          console.error(`Failed to send email to ${user.email}:`, error);
        }
      );
    });
  }

  deleteUser(): void {
    const selectedUsers = this.selection.selected.map(user => user.idUser);

    if (selectedUsers.length === 0) {
      console.warn("No users selected for deletion.");
      return;
    }

    selectedUsers.forEach(id => {
      this.secreService.deleteOtherUser(id).subscribe(
        () => this.ngOnInit(), // Recharger les utilisateurs après chaque suppression
        error => console.error(`Error deleting user with ID ${id}:`, error)
      );
    });
    // Clear the selection after deletion
    this.selection.clear();
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
