import { Injectable } from '@angular/core';  
import { HttpClient } from '@angular/common/http';  
import { Observable } from 'rxjs';  

@Injectable({  
  providedIn: 'root'  
})  
export class DataService {  
  private apiUrl = 'assets/data.json'; // Chemin relatif vers le fichier JSON  

  constructor(private http: HttpClient) { }  

  fetchData(): Observable<any> {  
    return this.http.get<any>(this.apiUrl);  
  }  
}