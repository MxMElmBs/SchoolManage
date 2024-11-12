import { Injectable } from '@angular/core';
import { Etudiant } from '../MODELS/etudiant';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})
export class DataSharingServiceService {
  private etudiantsSource = new BehaviorSubject<Etudiant[]>([]);
  etudiants$ = this.etudiantsSource.asObservable();

  updateEtudiants(etudiants: Etudiant[]) {
    this.etudiantsSource.next(etudiants);
  }
}
