import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Encadreur } from '../models/types';

@Injectable({
  providedIn: 'root'
})
export class EncadreurService {
  private apiUrl = 'http://localhost:8080/api/encadreurs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Encadreur[]> {
    return this.http.get<Encadreur[]>(this.apiUrl);
  }

  getById(id: number): Observable<Encadreur> {
    return this.http.get<Encadreur>(`${this.apiUrl}/${id}`);
  }

  create(encadreur: Encadreur): Observable<Encadreur> {
    return this.http.post<Encadreur>(this.apiUrl, encadreur);
  }

  update(id: number, encadreur: Encadreur): Observable<Encadreur> {
    return this.http.put<Encadreur>(`${this.apiUrl}/${id}`, encadreur);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
