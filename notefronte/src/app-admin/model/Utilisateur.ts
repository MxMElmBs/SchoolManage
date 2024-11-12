export class Utilisateur {
  constructor(
    public idUser: number = 0,
    public nom: string = '',
    public prenom: string = '',
    public username: string = '',
    public password: string = '',
    public email: string = '',
    public actif: string = '',
  ) {}
}
