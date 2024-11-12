import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Document } from '../models/Document';

@Injectable({
  providedIn: 'root'
})
export class DocumentselectService {
  // Utilisation de BehaviorSubject avec un document initial vide
  private selectedDocumentSource = new Document();

  constructor() { }

  // Met à jour le document sélectionné
  setSelectedDocument(document: Document) {
    this.selectedDocumentSource = document;
  }

  // Récupère le document sélectionné
  getSelectedDocument() {
    return this.selectedDocumentSource;
  }
}
