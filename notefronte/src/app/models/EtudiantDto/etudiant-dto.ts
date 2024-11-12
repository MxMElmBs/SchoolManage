import { NiveauEtude } from "../enums/niveau-etude";
import { Filiere } from "../filiere/filiere";

export interface EtudiantDto {
  etudiantId: number;
  matricule: string;
  nom: string;
  prenom: string;
  dateNaiss: string;
  niveauEtude: NiveauEtude;
  filiere: Filiere;
}
