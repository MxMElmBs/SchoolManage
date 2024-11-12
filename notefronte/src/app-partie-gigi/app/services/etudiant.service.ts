// etudiant.service.ts  
import { Injectable } from '@angular/core';  
import { HttpClient } from '@angular/common/http';  
import { Observable } from 'rxjs';  

@Injectable({  
    providedIn: 'root'  
})  
export class EtudiantService {  
    private apiUrl = 'URL_DE_VOTRE_API/etudiants'; // Remplacez par l'URL de votre API  

    constructor(private http: HttpClient) {}  

   // getEtudiants(): Observable<Etudiant[]> {  
       // return this.http.get<Etudiant[]>(this.apiUrl);  
    //}  
}