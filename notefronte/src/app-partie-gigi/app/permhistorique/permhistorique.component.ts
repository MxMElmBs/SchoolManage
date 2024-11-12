import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-permhistorique',
  standalone: true, // Composant standalone
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule
  ],
  templateUrl: './permhistorique.component.html',
  styleUrls: ['./permhistorique.component.css']
})
export class PermhistoriqueComponent implements OnInit {
  dataSource: any[] = [];
  selectedPermission: any = null; // Variable pour stocker la permission sélectionnée
  sortAscending: boolean = true; // Variable pour suivre le tri

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const etudiantId = localStorage.getItem('idetudiant');
    this.http.get<any[]>(`http://localhost:8060/api/auth/etudiant/permission-by-etudiant/${etudiantId}`)
      .subscribe(data => {
        console.log('Data received:', data);
        this.dataSource = data;
        this.cdr.detectChanges(); // Détection des changements
      });
  }

  // Fonction pour afficher les détails d'une permission sélectionnée
  showDetails(permission: any) {
    this.selectedPermission = permission;
    console.log('Permission selected:', permission);
  }

  // Fonction pour télécharger le fichier associé à une permission
  downloadFile(permissionId: number) {
    const url = `http://localhost:8060/api/auth/download/${permissionId}`;
    this.http.get(url, { responseType: 'blob' })
      .subscribe(blob => {
        saveAs(blob, `permission-${permissionId}.pdf`);
      }, error => {
        console.error('Erreur lors du téléchargement du fichier', error);
      });
  }

  // Fonction pour trier les données par la clé (dateDemande ou dateFinAbsence)
  sortData(key: string) {
    this.sortAscending = !this.sortAscending; // Bascule entre ascendant et descendant
    this.dataSource.sort((a, b) => {
      const dateA = new Date(a[key]).getTime();
      const dateB = new Date(b[key]).getTime();
      return this.sortAscending ? dateA - dateB : dateB - dateA;
    });
  }
}
