import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoEscena, TipoEscenaCreateRequest } from '../models/tipo-escena';

@Injectable({
  providedIn: 'root'
})
export class TipoEscenaService {
  private apiUrl = '/api/tipos-escena'; // Usar ruta relativa para Docker

  constructor(private http: HttpClient) {}

  getTiposEscena(): Observable<TipoEscena[]> {
    return this.http.get<TipoEscena[]>(this.apiUrl);
  }

  getTipoEscenaById(id: number): Observable<TipoEscena> {
    return this.http.get<TipoEscena>(`${this.apiUrl}/${id}`);
  }

  createTipoEscena(tipoEscena: TipoEscenaCreateRequest): Observable<TipoEscena> {
    return this.http.post<TipoEscena>(this.apiUrl, tipoEscena);
  }

  updateTipoEscena(id: number, tipoEscena: TipoEscenaCreateRequest): Observable<TipoEscena> {
    return this.http.put<TipoEscena>(`${this.apiUrl}/${id}`, tipoEscena);
  }

  deleteTipoEscena(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
