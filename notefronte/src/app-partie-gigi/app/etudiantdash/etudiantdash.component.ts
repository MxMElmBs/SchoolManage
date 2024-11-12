import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { ChartData, ChartType } from 'chart.js';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-etudiantdash',
  standalone: true,
  imports: [CommonModule ,FormsModule,MatIconModule,NgOptimizedImage, RouterLink],
  templateUrl: './etudiantdash.component.html',
  styleUrl: './etudiantdash.component.css'
})
export class EtudiantdashComponent {
  /*dataSource = [{ date: 'Dupont', heure: 'Jean'},]*/

  dataSource2 = [
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de présence : MTH101'},
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de cahier de texte : MTH101'},
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de présence : MTH101' },
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de cahier de texte : MTH101'},
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de présence : MTH101' },
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de cahier de texte : MTH101' },
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de présence : MTH101' },
    { date: '12-07-2024', heure: '10:OO AM', notif: 'Enregistrement de cahier de texte : MTH101' },
  ];


  view() {
    console.log('View button clicked');
  }

}
