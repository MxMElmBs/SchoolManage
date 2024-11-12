import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatFormField, MatLabel, MatOption, MatSelect } from '@angular/material/select';


@Component({
  selector: 'app-ajoutfiliere',
  standalone: true,
  imports: [MatSelect, MatLabel, MatOption, MatFormField],
  templateUrl: './ajoutfiliere.component.html',
  styleUrl: './ajoutfiliere.component.css'
})
export class AjoutfiliereComponent {

  ueForm: FormGroup;
  selected = 'option2';



  constructor(private fb: FormBuilder) {
    this.ueForm = this.fb.group({
      filiere: ['', Validators.required],
      ues: this.fb.array([])
    });
  }

  get ues(): FormArray {
    return this.ueForm.get('ues') as FormArray;
  }

  ajouterUE() {
    this.ues.push(this.fb.group({
      nom: ['', Validators.required],
      heures: ['', [Validators.required, Validators.min(1)]]
    }));
  }

  soumettre() {
    if (this.ueForm.valid) {
      console.log(this.ueForm.value);
      // Ici, vous pouvez envoyer les données au serveur ou les traiter
    }
  }
}
