export class EtudiantDocDto {
    constructor(
        public id: number = 0,
        public nom: string = '',
        public prenom: string = '',
        public email: string = '',
        public matricule: string = ''
    ) {}
}
