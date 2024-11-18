import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatCommonModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-resumepresence',
  standalone: true,
  imports: [
    RouterOutlet, RouterLink, MatCommonModule, MatCardModule, MatTableModule, MatGridListModule, MatIconModule, CommonModule
  ],
  templateUrl: './resumepresence.component.html',
  styleUrls: ['./resumepresence.component.css']
})
export class ResumepresenceComponent implements OnInit {
  ueId: string = '';  
  absenceInfo: any = {};  // Pour stocker les données de l'API "/ue/{ueId}/absence-info"
  seances: any[] = [];  // Historique des présences

  displayedColumns: string[] = ['matricule', 'nom', 'prenom', 'etat'];

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router ) {}

  ngOnInit() {
    this.ueId = this.route.snapshot.paramMap.get('id') as string;

    this.http.get(`http://localhost:8060/api/auth/de/ue/${this.ueId}/absence-info`).subscribe((data: any) => {
      this.absenceInfo = data;
    });

    // Appeler l'API pour récupérer l'historique des présences
    this.http.get<any[]>(`http://localhost:8060/api/auth/de/historique-presence/ue/${this.ueId}`).subscribe((data: any[]) => {
      this.seances = data;
    });
  }

/*   getTauxPresence(): number {
    const totalPresences = this.seances.reduce((sum: number, seance: any) =>
      sum + seance.presences.filter((presence: any) => presence.present).length, 0);
    const totalEtudiants = this.seances.reduce((sum: number, seance: any) => sum + seance.presences.length, 0);
    return totalEtudiants > 0 ? (totalPresences / totalEtudiants) * 100 : 0;
  } */

  goToDetails() {
    // Naviguer vers la page des détails de la classe
    this.router.navigate([`/app-gigi/dashboard/classedetails/${this.ueId}`]);
  }
}
