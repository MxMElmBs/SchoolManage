export class EtudiantDetailsDto {
    nom: string;
    prenom: string;
    parcours: string;
    filiere: string;
    theme: string;
    typeDocument: string;
    annee: string;  // Ajout de l'année
    documentUrl: string;
    professeurNom: string;
    professeurPrenom: string;
      constructor() {
        this.nom = '';
        this.prenom = '';
        this.parcours = '';
        this.filiere = '';
        this.theme = '';
        this.typeDocument = '';
        this.annee = '';
        this.documentUrl = '';
        this.professeurNom = '';
        this.professeurPrenom = '';
      }
    
  }
  