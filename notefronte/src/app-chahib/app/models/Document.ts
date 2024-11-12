import { TypeDocument } from './TypeDocument';
import { Professeur } from './Professeur';
import { Etudiant } from './Etudiant';

export class Document {
  id: number;
  theme: string;
  introduction: string;
  resumeDiaClasse: string;
  resumeDiaSequence: string;
  resumeDoc: string;
  conclusion: string;
  createdAt: Date;
  documentUrl: string;
  typeDocument: TypeDocument;
  professeur: Professeur;
  etudiant: Etudiant;  // Étudiant contient les informations de parcours et filière

  constructor() {
    this.id = 0;
    this.theme = '';
    this.introduction = '';
    this.resumeDiaClasse = '';
    this.resumeDiaSequence = '';
    this.resumeDoc = '';
    this.conclusion = '';
    this.createdAt = new Date();
    this.documentUrl = '';
    this.typeDocument = TypeDocument.Memoire;  // Par défaut
    this.professeur = new Professeur();
    this.etudiant = new Etudiant();  // Pas besoin de parcours/filière explicitement
  }
}
