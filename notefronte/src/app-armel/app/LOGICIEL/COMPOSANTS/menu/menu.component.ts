import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterLink,
    
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  activeLink: string = ''; // Par défaut, aucun lien n'est actif

  setActive(link: string) {
    this.activeLink = link; // Définit le lien actif
}
}
