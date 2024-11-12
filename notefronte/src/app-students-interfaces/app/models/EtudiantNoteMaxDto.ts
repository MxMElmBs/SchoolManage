export class EtudiantNoteMaxDto {
  constructor(
    public ueCode: string = '',
    public ueLibelle: string = '',
    public devoir: number = 0,
    public examen: number = 0,
    public typeUe: string = '',
    public semestre: string = '',
  ) {}
}
