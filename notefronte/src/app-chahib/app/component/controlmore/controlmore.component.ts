import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';  
import { DocumentService } from '../../services/document-service.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Document } from '../../models/Document';
import { Professeur } from '../../models/Professeur';
import { DocumentselectService } from '../../services/documentselect.service';

@Component({
  selector: 'app-controlmore',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './controlmore.component.html',
  styleUrls: ['./controlmore.component.css']
})
export class ControlmoreComponent implements OnInit {
  document: Document = new Document();  
  documentId: number = 1; // Par exemple, ID statique ou récupéré dynamiquement
  theme: string = '';                   
  typeDocument: string = '';            
  selectedProfesseur: Professeur | null = null;  
  professeurs: Professeur[] = [];       
  isThemeValid: boolean = true;         
  isDisabled: boolean = true; // Champs désactivés par défaut     
  selectDocs: Document = new Document();      
  

  constructor(
    private documentService: DocumentService,
    private router: Router, // Pour la navigation
    private docsSelect: DocumentselectService
  ) {}

  ngOnInit(): void {
    this.selectDocs = this.docsSelect.getSelectedDocument();
    this.documentId = this.selectDocs.id;
    console.log('Document sélectionné :', this.selectDocs);
    this.loadProfesseur();  // Charger le professeur d'abord
  }

  // Charger le professeur associé au document par son ID
  loadProfesseur() {
    this.documentService.getProfesseurByDocumentID(this.documentId).subscribe(
      (professeur: Professeur) => {
        this.selectedProfesseur = professeur;  // Stocker le professeur récupéré
        console.log('Professeur associé :', this.selectedProfesseur);
        this.loadDocument();  // Charger le document après avoir récupéré le professeur
      },
      (error) => {
        console.error('Erreur lors de la récupération du professeur', error);
        this.selectedProfesseur = null;  // Si aucune donnée n'est trouvée
      }
    );
  }

  // Charger le document par son ID
  loadDocument() {
    this.documentService.getDocumentById(this.documentId).subscribe(
      (document: Document) => {
        this.document = document;
        this.theme = this.document.theme;
        this.typeDocument = this.document.typeDocument;
        console.log('Document chargé :', this.document);
      },
      (error: any) => {
        console.error('Erreur lors de la récupération du document', error);
      }
    );
  }

  // Rediriger vers le formulaire si l'utilisateur ne souhaite pas modifier
  handleSubmit() {
    this.router.navigate(['/app-chahib/formulaire', this.documentId]);  // Naviguer vers le formulaire
  }

  // Activer les champs pour permettre la modification et rediriger vers la page d'update
  enableEdit() {
    this.router.navigate(['/app-chahib/update-control', this.documentId]);  // Rediriger vers la page de mise à jour
  }
}
