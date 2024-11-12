export class EtudiantConnect {
    constructor(
      public id: number =0,
      public matricule: string = '',
      public nom: string = '',
      public prenom: string = '',
      public email: string = '',
      public lieuNaiss: string = '',
      public sexe: string = '',
      public dob: string = '',
      public parcours: string = '',
      public niveau: string = '',
    ) {}
}
