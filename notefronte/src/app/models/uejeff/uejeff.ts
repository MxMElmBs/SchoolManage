import { TypeNote } from "../enums/type-note";

export interface Uejeff {
  id: number;
  code: string;
  libelle: string;
  type: string;
  description: string;
  credit: number;
  niveauEtude: string;
  typeSemestre: string;
  professeurId: number;
  professeurName: string;
  professeurPrenom: string;
  professeurEmail: string;
  filiereIds: number[];
  typeNote: TypeNote;
}
