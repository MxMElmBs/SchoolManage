import { CommonModule } from '@angular/common';  
import { Component } from '@angular/core'; 
import { FormsModule } from '@angular/forms';  

@Component({  
  selector: 'app-cahier',  
  standalone: true,  
  imports: [FormsModule, CommonModule],  
  templateUrl: './cahier.component.html',  
  styleUrls: ['./cahier.component.css'] // Notez qu'il faut utiliser 'styleUrls' et non 'styleUrl'  
})  
export class CahierComponent {  
  ues: Array<{ filiere: string, nom: string,semestre: number, heures: number }> = [];  
  filiere: string = '';  
  nom: string = ''; 
  semestre: number | null=null; 
  heures: number | null = null;  

  ngOnInit() {  
    this.creerUesFictives();  
  }  

  creerUesFictives() {  
    this.ues = [  
      { filiere: 'Informatique', nom: 'Programmation Web', heures: 30 , semestre:2 },  
      { filiere: 'Informatique', nom: 'Bases de données', heures: 25, semestre:2 },  
      { filiere: 'Mathématiques', nom: 'Analyse Mathématique', heures: 40 , semestre:2},  
      { filiere: 'Mathématiques', nom: 'Algèbre Linéaire', heures: 35 , semestre:2},   
      { filiere: 'Physique', nom: 'Mécanique', heures: 30 , semestre:2},  
      { filiere: 'Physique', nom: 'Électromagnétisme', heures: 35, semestre:2 }  
    ];  
  }  
// Exemple de structure de données 
  ajouterUE() {  
    if (this.filiere && this.nom && this.heures && this.semestre) {  
      this.ues.push({ filiere: this.filiere, nom: this.nom, heures: this.heures, semestre:this.semestre });  
      this.resetForm();  
    } else {  
      alert('Veuillez remplir tous les champs.');  
    }  
  }  

  soumettre() {  
    console.log(this.ues);  
    alert('Données soumises : ' + JSON.stringify(this.ues));  
  }  

  resetForm() {  
    this.filiere = '';  
    this.nom = '';  
    this.heures = null;  
  }  

  // Fonction pour obtenir les UEs groupées par filière  
  getUesParFiliere() {  
    return this.ues.reduce((acc, ue) => {  
      if (!acc[ue.filiere]) {  
        acc[ue.filiere] = [];  
      }  
      acc[ue.filiere].push(ue);  
      return acc;  
    }, {} as Record<string, { nom: string; heures: number }[]>);  
  }
  // Liste des filières  
  filieres = [  
    { name: 'Genie Logiciel', selected: false },  
    { name: 'Système réseau', selected: false },  
    { name: 'GL&SR', selected: false },  
  ];  

  // Méthode pour afficher les filières sélectionnées  
  afficherSelection() {  
    const selection = this.filieres.filter(filiere => filiere.selected).map(filiere => filiere.name);  
    console.log('Filières sélectionnées:', selection);  
  }  

}