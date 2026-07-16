import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/types';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  private apiUrl = '/api/etudiants';

  constructor(private http: HttpClient) {}

  getAll(search?: string): Observable<Etudiant[]> {
    if (search && search.trim() !== '') {
      return this.http.get<Etudiant[]>(`${this.apiUrl}?search=${search}`);
    }
    return this.http.get<Etudiant[]>(this.apiUrl);
  }

  getById(id: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.apiUrl}/${id}`);
  }

  create(etudiant: Etudiant): Observable<Etudiant> {
    return this.http.post<Etudiant>(this.apiUrl, etudiant);
  }

  update(id: number, etudiant: Etudiant): Observable<Etudiant> {
    return this.http.put<Etudiant>(`${this.apiUrl}/${id}`, etudiant);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
