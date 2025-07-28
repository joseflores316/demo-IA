import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Actriz, ActrizCreateRequest, Pais } from '../models/actriz';

@Injectable({
  providedIn: 'root'
})
export class ActrizService {
  private readonly baseUrl = '/api/actrices'; // Usar ruta relativa para Docker

  constructor(private http: HttpClient) {}

  // Obtener todas las actrices
  getActrices(): Observable<Actriz[]> {
    return this.http.get<Actriz[]>(this.baseUrl);
  }

  // Obtener actriz por ID
  getActrizById(id: number): Observable<Actriz> {
    return this.http.get<Actriz>(`${this.baseUrl}/${id}`);
  }

  // Crear actriz
  createActriz(actriz: ActrizCreateRequest): Observable<Actriz> {
    return this.http.post<Actriz>(this.baseUrl, actriz);
  }

  // Crear actriz con imagen
  createActrizConImagen(actriz: ActrizCreateRequest, imagen: File): Observable<Actriz> {
    const formData = new FormData();
    formData.append('actriz', JSON.stringify(actriz));
    formData.append('imagen', imagen);

    return this.http.post<Actriz>(`${this.baseUrl}/con-imagen`, formData);
  }

  // Actualizar actriz
  updateActriz(id: number, actriz: ActrizCreateRequest): Observable<Actriz> {
    return this.http.put<Actriz>(`${this.baseUrl}/${id}`, actriz);
  }

  // Actualizar actriz con imagen
  updateActrizConImagen(id: number, actriz: ActrizCreateRequest, imagen?: File): Observable<Actriz> {
    const formData = new FormData();
    formData.append('actriz', JSON.stringify(actriz));
    if (imagen) {
      formData.append('imagen', imagen);
    }

    return this.http.put<Actriz>(`${this.baseUrl}/${id}/con-imagen`, formData);
  }

  // Eliminar actriz
  deleteActriz(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar actrices por nombre
  buscarPorNombre(nombre: string): Observable<Actriz[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<Actriz[]>(`${this.baseUrl}/buscar`, { params });
  }

  // Filtrar por calificación
  filtrarPorCalificacion(minCalificacion: number): Observable<Actriz[]> {
    const params = new HttpParams().set('minCalificacion', minCalificacion.toString());
    return this.http.get<Actriz[]>(`${this.baseUrl}/filtrar`, { params });
  }

  // Obtener países
  getPaises(): Observable<Pais[]> {
    return this.http.get<Pais[]>('/api/paises'); // Usar ruta relativa para Docker
  }
}
