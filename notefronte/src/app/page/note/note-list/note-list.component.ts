import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Uejeff } from '../../../models/uejeff/uejeff';
import { NoteEtudiantDto } from '../../../models/NoteEtudiantDto/note-etudiant-dto';
import { NoteService } from '../../../services/NoteService/note.service';
import { SaisieNoteDto } from '../../../models/SaisieNote/saisie-note-dto';
import { CommonModule } from '@angular/common';
import { TypeNote } from '../../../models/enums/type-note';
import { UejeffService } from '../../../services/UejeffService/uejeff.service';
import { TypeSemestre } from '../../../models/enums/type-semestre';
import { FormsModule } from '@angular/forms';
import $ from 'jquery';
import 'datatables.net';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-note-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './note-list.component.html',
  styleUrl: './note-list.component.css'
})
export class NoteListComponent implements OnInit, AfterViewInit {
  uesBySemestre: { [key: string]: Uejeff[] } = {
    SEMESTRE_1: [],
    SEMESTRE_2: [],
    SEMESTRE_3: [],
    SEMESTRE_4: [],
    SEMESTRE_5: [],
    SEMESTRE_6: []
  };
  notes: NoteEtudiantDto[] = [];
  ueId!: number; // L'UE sélectionnée pour la modification
  typeNote!: TypeNote;
  semestre!: TypeSemestre;
  selectedUeLibelle!: string;
  selectedTypeNote!: TypeNote;

  dataTablesInstances: any = {};  // Stocke les instances de DataTables
  dataLoaded: boolean = false;  // Drapeau pour vérifier si les données sont chargées

  constructor(private noteService: NoteService, private ueService: UejeffService) { }

  ngOnInit(): void {
    this.loadUesForSemestres();
  }

  // Charger les UE pour tous les semestres
  loadUesForSemestres() {
    const semestres = ['SEMESTRE_1', 'SEMESTRE_2', 'SEMESTRE_3', 'SEMESTRE_4', 'SEMESTRE_5', 'SEMESTRE_6'];
    let loadedSemestres = 0;

    semestres.forEach(semestre => {
      this.loadUesBySemestre(semestre as TypeSemestre, () => {
        loadedSemestres++;
        if (loadedSemestres === semestres.length) {
          this.dataLoaded = true;  // Toutes les données sont chargées
          this.initializeDataTables();  // Initialiser DataTables après chargement des données
        }
      });
    });
  }

  // Charger les UE par semestre et appeler la callback après chargement
  loadUesBySemestre(semestre: TypeSemestre, callback: () => void) {
    this.ueService.getUesWithNotesBySemestre(semestre).subscribe((ues: Uejeff[]) => {
      this.uesBySemestre[semestre] = ues;
      callback();  // Appeler la callback pour indiquer que ce semestre est chargé
    });
  }

  // Initialiser ou réinitialiser DataTables
  initializeDataTables() {
    const semestres = ['SEMESTRE_1', 'SEMESTRE_2', 'SEMESTRE_3', 'SEMESTRE_4', 'SEMESTRE_5', 'SEMESTRE_6'];

    semestres.forEach(semestre => {
      this.reinitializeDataTableForSemestre(semestre as TypeSemestre);
    });
  }

  // Méthode pour réinitialiser DataTable après chargement des données
  reinitializeDataTableForSemestre(semestre: TypeSemestre) {
    const tableId = `#semestre${semestre[semestre.length - 1]}`;  // Identifiant du tableau (e.g., #semestre1)

    // Détruire l'instance DataTable si elle existe déjà
    if (this.dataTablesInstances[tableId]) {
      this.dataTablesInstances[tableId].destroy();
    }

    // Réinitialiser la DataTable après un léger délai pour s'assurer que les données sont rendues
    setTimeout(() => {
      this.dataTablesInstances[tableId] = $(tableId).DataTable({
        language: {
          url: 'https://cdn.datatables.net/plug-ins/1.13.5/i18n/fr-FR.json'
        },
        pagingType: 'full_numbers',
        destroy: true  // S'assurer que les tables peuvent être réinitialisées
      });
    }, 100);  // Délai pour s'assurer que les nouvelles données sont rendues
  }

  // Modifier les notes d'une UE
  onModifierNotes(ueId: number, typeNote: TypeNote) {
    this.ueId = ueId;
    this.typeNote = typeNote;

    // Récupérer les notes des étudiants
    this.noteService.getNotesEtudiantsParUEEtTypeNote(ueId, typeNote).subscribe((notes: NoteEtudiantDto[]) => {
      this.notes = notes;
      console.log('Notes récupérées', this.notes);  // Vérifie si les notes sont correctement récupérées
    });
  }

  // Enregistrer les modifications de notes
  onEnregistrerNotes() {
    // Convertir les NoteEtudiantDto en SaisieNoteDto
    const saisieNotes: SaisieNoteDto[] = this.notes.map(note => ({
      etudiantId: note.etudiantId,
      ueId: this.ueId,  // Assurez-vous que ueId est bien défini avant l'appel
      typeNote: this.typeNote,  // Assurez-vous que typeNote est bien défini avant l'appel
      valeurNote: note.valeur
    }));

    console.log('Données envoyées pour modification :', saisieNotes);  // Ajoute un log pour vérifier les données

    this.noteService.modifierNotes(saisieNotes).subscribe({
      next: (response) => {
        console.log('Réponse du serveur:', response);
        // Afficher SweetAlert en cas de succès
        Swal.fire({
          icon: 'success',
          title: 'Modification réussie',
          text: 'Les notes ont été modifiées avec succès !',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'OK'
        });
      },
      error: (error) => {
        console.error('Erreur lors de la modification des notes', error);
      }
    });
  }

  // Ouvrir le modal de suppression
  openSupprimerModal(ue: Uejeff, typeNote: TypeNote) {
    this.ueId = ue.id // Stocker l'ID de l'UE
    this.selectedUeLibelle = ue.libelle; // Stocker le libellé de l'UE
    this.selectedTypeNote = typeNote; // Stocker le type de note
  }

  // Supprimer les notes d'une UE par type de note
  onConfirmerSuppression() {
    this.noteService.supprimerNotes(this.ueId, this.selectedTypeNote).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Suppression réussie',
          text: 'Les notes ont été supprimées avec succès !',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'OK'
        });
        this.loadUesForSemestres(); // Rafraîchir les UE après suppression
      },
      error: (error) => {
        Swal.fire({
          icon: 'success',
          title: 'Suppression réussie',
          text: 'Les notes ont été supprimées avec succès !',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'OK'
        }).then(() => {
          // Rafraîchir la page après avoir appuyé sur le bouton "OK"
          window.location.reload();
        });
        console.error('Erreur lors de la suppression des notes', error);
      }
    });
  }

  ngAfterViewInit() {
    // Initialisation des DataTables après le rendu de la vue si les données sont déjà chargées
    if (this.dataLoaded) {
      this.initializeDataTables();
    }
  }
}
