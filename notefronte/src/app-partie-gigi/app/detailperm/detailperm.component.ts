import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar'; // Pour afficher les notifications
import { PopupComponent } from '../popup/popup.component';
import { saveAs } from 'file-saver';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PermpopupComponent } from '../permpopup/permpopup.component';

@Component({
  selector: 'app-detailperm',
  standalone: true,
  templateUrl: './detailperm.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./detailperm.component.css']
})
export class DetailpermComponent implements OnInit {
  permission: any = {};
  directeurEtudeId: number = 1;

  constructor(
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const permissionId = this.route.snapshot.paramMap.get('id');
    this.http.get<any>(`http://localhost:8060/api/auth/de/permission-by-id/${permissionId}`).subscribe(
      (response) => {
        this.permission = response;
      },
      (error) => {
        this.showMessage('Erreur lors du chargement de la permission', 'error');
      }
    );
  }

  // Méthode pour télécharger le fichier
  downloadFile(permissionId: number): void {
    const downloadUrl = `http://localhost:8060/api/auth/de/download-permfile/${permissionId}`;
  
    // Effectuer une requête pour récupérer l'URL de redirection
    this.http.get(downloadUrl, { observe: 'response', responseType: 'text' }).subscribe(
      (response) => {
        const redirectUrl = response.url;  // L'URL de redirection peut être dans l'en-tête ou la réponse
        if (redirectUrl) {
          // Rediriger le navigateur vers l'URL du fichier
          window.location.href = redirectUrl;
          this.showMessage('Le téléchargement a commencé', 'success');
        } else {
          this.showMessage('Erreur : URL de redirection introuvable', 'error');
        }
      },
      (error) => {
        this.showMessage('Erreur lors de la récupération du fichier', 'error');
      }
    );
  }
  

  // Méthode pour approuver une permission avec remarque facultative
  // Méthode pour approuver une permission avec remarque facultative
  openApprovePopup(): void {
    const dialogRef = this.dialog.open(PermpopupComponent, {
      data: { type: 'approve' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.approvePermission(result.inputText || '');
      }
      // Recharger la page après la fermeture de la popup (5s après ouverture)
      setTimeout(() => {
        window.location.reload();
      }, 500); // Recharge 0,5 seconde après fermeture de la popup
    });
  }

  // Méthode pour rejeter une permission avec raison facultative
  openRejectPopup(): void {
    const dialogRef = this.dialog.open(PermpopupComponent, {
      data: { type: 'reject' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.rejectPermission(result.inputText || '');
      }
      // Recharger la page après la fermeture de la popup (5s après ouverture)
      setTimeout(() => {
        window.location.reload();
      }, 500); // Recharge 0,5 seconde après fermeture de la popup
    });
  }

  // Appel API pour approuver la permission
  approvePermission(remarque: string): void {
    const url = `http://localhost:8060/api/auth/de/approuver/${this.permission.permissionId}/${this.directeurEtudeId}`;
    this.http.put(url, { remarque }, { responseType: 'json' }).subscribe(
      (response) => {
        this.showMessage('Permission approuvée avec succès', 'success');
      },
      (error) => {
        this.showMessage('Erreur lors de l\'approbation de la permission', 'error');
      }
    );
  }

  // Appel API pour rejeter la permission
  rejectPermission(raison: string): void {
    const url = `http://localhost:8060/api/auth/de/rejeter/${this.permission.permissionId}/${this.directeurEtudeId}`;
    this.http.put(url, { raison }, { responseType: 'json' }).subscribe(
      (response) => {
        this.showMessage('Permission rejetée avec succès', 'success');
      },
      (error) => {
        this.showMessage('Erreur lors du rejet de la permission', 'error');
      }
    );
  }

  // Méthode pour afficher les messages de succès ou d'erreur
  showMessage(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, '', {
      duration: 5000,
      panelClass: type === 'success' ? 'snack-bar-success' : 'snack-bar-error'
    });
  }
}
