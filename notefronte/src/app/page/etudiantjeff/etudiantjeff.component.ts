import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AsyncPipe, CommonModule} from '@angular/common';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import $ from 'jquery';
import 'datatables.net';
import { EtudiantjeffService } from '../../services/EtudiantjeffService/etudiantjeff.service';
import { NoteService } from '../../services/NoteService/note.service';
import { NoteDto } from '../../models/NoteDto/note-dto';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-etudiantjeff',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatAutocompleteModule, ReactiveFormsModule, AsyncPipe],
  templateUrl: './etudiantjeff.component.html',
  styleUrl: './etudiantjeff.component.css'
})

export class EtudiantjeffComponent implements AfterViewInit {



  @ViewChild('input')
  input!: ElementRef<HTMLInputElement>;
  myControl = new FormControl('');
  etudiants: { id: number, nom: string, prenom: string }[] = []; // Liste des étudiants avec ID
  filteredOptions: { id: number, nom: string, prenom: string }[] = [];
  selectedEtudiantId: number | null = null; // Stocker l'ID de l'étudiant sélectionné
  notes: NoteDto[] = [];
  dataTableInstance: any;
  noteToEdit: NoteDto | null = null;

  constructor(private etudiantService: EtudiantjeffService, private noteService: NoteService) {
  }

  filter(): void {
    const filterValue = this.input.nativeElement.value.toLowerCase();
    this.filteredOptions = this.etudiants.filter(option =>
      `${option.nom} ${option.prenom}`.toLowerCase().includes(filterValue)
    );
  }

  ngAfterViewInit(): void {
    this.etudiantService.getEtudiantsWithNotes().subscribe(etudiants => {
      this.etudiants = etudiants.map(e => ({ id: e.id, nom: e.nom, prenom: e.prenom }));
      this.filteredOptions = [...this.etudiants];

    });
  }

  // Méthode pour réinitialiser le DataTable
  private reinitializeDataTable(): void {
    if (this.dataTableInstance) {
      this.dataTableInstance.destroy(); // Détruire l'instance existante
    }

    // Réinitialiser le DataTable après un léger délai pour s'assurer que les données sont rendues
    setTimeout(() => {
      this.dataTableInstance = $('#x').DataTable({
        language: {
          url: 'https://cdn.datatables.net/plug-ins/1.13.5/i18n/fr-FR.json'
        },
        pagingType: 'full_numbers',
        destroy: true
      });
    }, 100);  // Un petit délai pour s'assurer que les nouvelles données sont bien rendues avant l'initialisation
  }

  // Trouver et charger les notes de l’étudiant sélectionné
  onTrouverEtudiant(): void {
    const selectedOption = this.input.nativeElement.value;
    const selectedEtudiant = this.filteredOptions.find(option => `${option.nom} ${option.prenom}` === selectedOption);

    if (selectedEtudiant) {
      this.selectedEtudiantId = selectedEtudiant.id;
      this.noteService.getNotesByEtudiant(this.selectedEtudiantId).subscribe(notes => {
        this.notes = notes;
        this.reinitializeDataTable();
      });
    }
  }

  // Ouvrir le modal de modification
  openEditModal(noteId: number): void {
    // Récupérer la note à partir du service
    this.noteService.getNoteById(noteId).subscribe({
      next: (note) => {
        this.noteToEdit = note; // Affecter la note récupérée
        // Ouvrir le modal après avoir récupéré la note
      },
      error: (err) => {
        console.error('Erreur lors du chargement de la note :', err);
      }
    });
  }

  // Mettre à jour la note sélectionnée
  onUpdateNote(): void {
    if (this.noteToEdit) {  // Vérifie que noteToEdit n'est pas null
      this.noteService.updateNoteEtudiant(this.noteToEdit.id, this.noteToEdit).subscribe(updatedNote => {
        this.onTrouverEtudiant();  // Rafraîchir la liste des notes
        $('#editNoteModal').modal('hide');  // Fermer le modal
      });
    } else {
      console.error('Aucune note à modifier');
    }
  }

  // Supprimer la note
  confirmDeleteNote(noteId: number): void {
    Swal.fire({
      title: 'Êtes-vous sûr?',
      text: "Vous ne pourrez pas annuler cela!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer!',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.noteService.deleteNote(noteId).subscribe(() => {
          this.onTrouverEtudiant(); // Recharger les notes après suppression
          Swal.fire(
            'Supprimé!',
            'La note a été supprimée.',
            'success'
          );
        }, error => {
          Swal.fire(
            'Erreur!',
            'Un problème est survenu lors de la suppression.',
            'error'
          );
        });
      }
    });
  }

  getAppreciation(note: number): string {
    if (note >= 0 && note < 10) return "INSUFFISANT";
    if (note >= 10 && note < 12) return "PASSABLE";
    if (note >= 12 && note < 14) return "ASSEZ-BIEN";
    if (note >= 14 && note < 16) return "BIEN";
    if (note >= 16 && note < 20) return "TRÈS BIEN";
    if (note === 20) return "EXCELLENT";
    return "INVALID";
  }

}



