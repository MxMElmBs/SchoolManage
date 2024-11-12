import { AfterViewInit, Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DataTablesModule } from 'angular-datatables';
import $ from 'jquery';
import 'datatables.net';
import { Uejeff } from '../../models/uejeff/uejeff';
import { Etudiantjeff } from '../../models/etudiantjeff/etudiantjeff';
import { TypeSemestre } from '../../models/enums/type-semestre';
import { TypeNote } from '../../models/enums/type-note';
import { UejeffService } from '../../services/UejeffService/uejeff.service';
import { EtudiantjeffService } from '../../services/EtudiantjeffService/etudiantjeff.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SaisieNoteDto } from '../../models/SaisieNote/saisie-note-dto';
import { NoteService } from '../../services/NoteService/note.service';
import { Collapse, Modal } from 'bootstrap';
import { NoteListComponent } from "./note-list/note-list.component";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-note',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule, ReactiveFormsModule, NoteListComponent],
  templateUrl: './note.component.html',
  styleUrl: './note.component.css'
})
export class NoteComponent implements OnInit {

  ues: Uejeff[] = [];
  etudiants: Etudiantjeff[] = [];
  saisieNotes: SaisieNoteDto[] = [];
  semestre!: TypeSemestre;
  typeNote!: TypeNote;
  ueId!: number;
  typeSemestreEnum = TypeSemestre;
  typeNoteEnum = TypeNote;
  noteForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private ueService: UejeffService,
    private etudiantService: EtudiantjeffService,
    private noteService: NoteService
  ) { }

  ngOnInit(): void {
    // Initialisation du formulaire avec des validateurs
    this.noteForm = this.fb.group({
      semestre: ['', Validators.required],
      ueId: ['', Validators.required],
      typeNote: ['', Validators.required]
    });

  }

  // Récupérer les UE en fonction du semestre sélectionné
  onSemestreChange() {
    const semestre = this.noteForm.get('semestre')?.value;
    if (semestre) {
      this.ueService.getUEsBySemestre(semestre).subscribe((data: Uejeff[]) => {
        this.ues = data;
      });
    }
  }

  // Récupérer les étudiants en fonction du semestre sélectionné
  rechercherEtudiants() {
    const semestre = this.noteForm.get('semestre')?.value;
    if (semestre) {
      this.etudiantService.getEtudiantsBySemestre(semestre).subscribe((data: Etudiantjeff[]) => {
        this.etudiants = data;
        console.log('Etudiants récupérés', data);
      });
    }
  }

  // Vérifier que toutes les sélections sont faites
  canSearch(): boolean {
    return this.noteForm.valid;
  }

  // Ajouter la note saisie pour chaque étudiant
  // Ajouter ou supprimer la note saisie pour chaque étudiant
  ajouterNote(etudiantId: number, event: Event) {
    const inputElement = event.target as HTMLInputElement;
    let valeurNote = inputElement.value;

    // Remplacer la virgule par un point pour la notation correcte
    valeurNote = valeurNote.replace(',', '.');

    const noteNumerique = parseFloat(valeurNote);  // Convertir la chaîne en nombre

    // Vérifier si la note est effacée ou invalide
    if (!valeurNote || isNaN(noteNumerique)) {
      // Supprimer la note si elle a été effacée
      const index = this.saisieNotes.findIndex(note => note.etudiantId === etudiantId && note.ueId === this.noteForm.get('ueId')?.value);
      if (index !== -1) {
        this.saisieNotes.splice(index, 1);  // Supprimer la note effacée
      }
      return;
    }

    // Si la note est dans la plage valide (0 à 20), l'ajouter ou la mettre à jour
    if (noteNumerique >= 0 && noteNumerique <= 20) {
      const saisieNote: SaisieNoteDto = {
        etudiantId: etudiantId,
        ueId: this.noteForm.get('ueId')?.value,
        valeurNote: noteNumerique,
        typeNote: this.noteForm.get('typeNote')?.value
      };

      const index = this.saisieNotes.findIndex(note => note.etudiantId === etudiantId && note.ueId === this.ueId);
      if (index !== -1) {
        this.saisieNotes[index] = saisieNote;  // Mettre à jour la note existante
      } else {
        this.saisieNotes.push(saisieNote);  // Ajouter la nouvelle note
      }
    } else {
      this.errorMessage = 'Veuillez entrer une note valide (entre 0 et 20) pour chaque étudiant.';
    }
  }

  // Enregistrer les notes au backend
  submitNotes() {
    this.errorMessage = ''; // Réinitialiser le message d'erreur au début

    if (this.saisieNotes.length === 0) {
        this.errorMessage = 'Aucune note saisie. Veuillez entrer des notes pour chaque étudiant.';
        return;
    }

    // Variables pour les erreurs
    const invalidNotes: string[] = []; // Pour stocker les noms des étudiants avec notes invalides
    const missingNotes: string[] = []; // Pour stocker les noms des étudiants sans notes

    // Vérifier d'abord si une note est invalide (en dehors de 0-20)
    for (const saisie of this.saisieNotes) {
        const etudiant = this.etudiants.find(e => e.id === saisie.etudiantId);
        if (isNaN(saisie.valeurNote) || saisie.valeurNote < 0 || saisie.valeurNote > 20) {
            if (etudiant) {
                invalidNotes.push(`${etudiant.nom} ${etudiant.prenom}`);
            }
        }
    }

    // Vérifier que chaque étudiant a bien une note dans saisieNotes
    for (const etudiant of this.etudiants) {
        const noteExists = this.saisieNotes.some(note => note.etudiantId === etudiant.id);
        if (!noteExists) {
            missingNotes.push(`${etudiant.nom} ${etudiant.prenom}`);
        }
    }

    // Construire le message d'erreur
    if (invalidNotes.length > 0) {
        this.errorMessage = 'Une ou plusieurs notes sont invalides pour les étudiants suivants : ' + invalidNotes.join(', ');
        return;
    }

    if (missingNotes.length > 0) {
        if (missingNotes.length === 1) {
            this.errorMessage = 'Veuillez saisir une note pour l\'étudiant : ' + missingNotes[0];
        } else {
            this.errorMessage += 'Veuillez saisir une note pour les étudiants suivants : ' + missingNotes.join(', ');
        }
        return;
    }

    // Appel au service pour enregistrer les notes
    this.noteService.enregistrerNotes(this.saisieNotes).subscribe({
      next: () => {
        // SweetAlert de succès
        Swal.fire({
          icon: 'success',
          title: 'Enregistrement réussi',
          text: 'Les notes ont été enregistrées avec succès !',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'OK'
        }).then(() => {
          this.saisieNotes = [];  // Réinitialiser la liste des notes
          this.resetForm();  // Réinitialiser le formulaire

          // Fermer le collapse après succès
          const collapseElement = document.getElementById('saisie');
          if (collapseElement) {
            const collapse = new Collapse(collapseElement, { toggle: false });
            collapse.hide();
          }
          window.location.reload();
        });
      },
      error: (error) => {
        console.error('Erreur lors de l\'enregistrement des notes', error);
        let errorMessage = 'Erreur lors de l\'enregistrement des notes.';

        if (error.error instanceof ErrorEvent) {
          // Erreur client ou réseau
          errorMessage = `Erreur : ${error.error.message}`;
        } else if (error.status === 500) {
          // Erreur serveur
          errorMessage = 'Notes déjà enregistrées pour cette UE.';
        }

        // SweetAlert d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Erreur :',
          text: errorMessage,
          confirmButtonColor: '#d33',
          confirmButtonText: 'OK'
        });
      }
    });
  }


  resetForm() {
    this.noteForm.reset();  // Réinitialise le formulaire de sélection
    this.ues = [];          // Vide la liste des UE
    this.etudiants = [];    // Vide la liste des étudiants
    this.saisieNotes = [];  // Vide les notes saisies
    this.errorMessage = ''; // Réinitialise le message d'erreur
  }


}
