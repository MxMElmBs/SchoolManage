import { Etudiant } from './Etudiant';
import { Professeur } from './Professeur';

export class Users {
    id: number;
    username: string;
    password: string;
    etudiant: Etudiant;
    professeur: Professeur;

    constructor() {
        this.id = 0;
        this.username = '';
        this.password = '';
        this.etudiant = new Etudiant(); // Initialiser avec une instance vide ou null
        this.professeur = new Professeur(); // Initialiser avec une instance vide ou null
    }
}
