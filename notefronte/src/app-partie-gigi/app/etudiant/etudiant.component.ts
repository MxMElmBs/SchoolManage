import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ChartData, ChartType } from 'chart.js';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-etudiant',
  standalone: true,
  imports: [CommonModule ,FormsModule,MatIconModule,MatCardModule],
  templateUrl: './etudiant.component.html',
  styleUrl: './etudiant.component.css'
})
export class EtudiantComponent {
  dataSource = [{ matricule:'00409L3',nom: 'Dupont', prenom: 'Jean'},]
  dataSource2 = [    
    { matricule:'00409L3',nom: 'AFI', prenom: 'madame'},
    { matricule:'00405L3',nom: 'ALI', prenom: 'Jean'},
  ]


  search() {
    // Implement search logic here
    console.log('Search clicked');
  }

  options = [
    { value: 'option1', label: '2023-2024' },
    { value: 'option2', label: '2023-2024' },
    { value: 'option3', label: '2023-2024' },
    { value: 'option4', label: '2023-2024' }
  ];
  options1 = [
    { value: 'option1', label: 'Genie Loogiciel' },
    { value: 'option2', label: 'Système & Réseau' },
  
  ];
  filiere = [
    { value: 'Toute filière', label: 'Toute filière' },
    { value: 'Genie logiciel', label: 'Genie logiciel' },
    { value: 'Système reseau', label: 'Système reseau' },
  ];
  parcours = [
    { value: 'Toute filière', label: 'Licence' },

  ];
  niveau = [
    { value: 'Toute filière ', label: '1' },
    { value: 'Genie logiciel', label: '2' },
    { value: 'Système reseau', label: '3' },
  ];

  selectedOption: string = this.options[0].value; // Option sélectionnée par défaut
  selectedOption1: string = this.options1[0].value;
  selectedfiliere: string = this.filiere[0].value;
  selectedniveau: string = this.niveau[0].value;

  onOptionChange(event: any) {
    this.selectedOption = event.target.value;
  }
}
