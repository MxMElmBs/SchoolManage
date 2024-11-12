import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatIconModule, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  menuItems = [
    { name: 'Tableau de bord', icon: 'dashboard' },
    { name: 'Étudiants', icon: 'school' },
    { name: 'Enseignants', icon: 'person' },
    { name: 'Directeurs d\'étude', icon: 'supervisor_account' },
    { name: 'Comptables', icon: 'account_balance'}
  ];


  constructor(private router: Router) {}

  navigateTo(name: string): void {
    if (name === 'Tableau de bord') {
      this.router.navigate(['/admin-dashboard/home']);
    }
    if (name === 'Étudiants') {
      this.router.navigate(['/admin-dashboard/etudiants']);
    }
    if (name === 'Enseignants') {
      this.router.navigate(['/admin-dashboard/profs']);
    }
    if (name === 'Directeurs d\'étude') {
      this.router.navigate(['/admin-dashboard/dse']);
    }
    if (name === 'Comptables') {
      this.router.navigate(['/admin-dashboard/comptables']);
    }
  }

}
