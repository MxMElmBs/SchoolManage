import { Filiere } from './Filiere';
import { Parcours } from './Parcours';
import { Users } from './Users';

export class Etudiant {
    id: number;
    nom: string;
    prenom: string;
    email: string;
    filiere: Filiere;
    nomparcours: Parcours;
    user: Users | null; // Autoriser null ou une instance

    constructor() {
        this.id = 0;
        this.nom = '';
        this.prenom = '';
        this.email = '';
        this.filiere = new Filiere();
        this.nomparcours = new Parcours();
        this.user = null; // Ne pas initialiser dans le constructeur pour éviter les références circulaires
    }
}
