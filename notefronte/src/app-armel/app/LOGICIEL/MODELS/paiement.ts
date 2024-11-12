
export interface Paiement {  etudiantNom: string;
  etudiantPrenom: string;
  etudiantMatricule: string;
  filiereNom: string;
  parcoursNom: string;
  niveauEtude: string;
  reductionMontantFinal: number;
  montantDejaPaye: number;
  typeModalite: string;
  montantActuel: number;
  //montantAchanger: number;
  resteEcolage: number;
  datePaiement: Date;
  etudiantId : number;
  statutScolarite: string;
  tuteurMail?: string; // Email du tuteur (facultatif)
  echeances: Array<{
    id: number;
    montantParEcheance: number;
    resteSurEcheance: number;
    dateEcheance: string;
    dateEnvoi: string;
    statut: string;
  }>;
}
