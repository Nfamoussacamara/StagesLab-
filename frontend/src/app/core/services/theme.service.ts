import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme, ThemeRequest } from '../models/types';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = 'http://localhost:8080/api/themes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Theme[]> {
    return this.http.get<Theme[]>(this.apiUrl);
  }

  getById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/${id}`);
  }

  create(theme: ThemeRequest): Observable<Theme> {
    return this.http.post<Theme>(this.apiUrl, theme);
  }

  update(id: number, theme: ThemeRequest): Observable<Theme> {
    return this.http.put<Theme>(`${this.apiUrl}/${id}`, theme);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
