import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Escena, EscenaCreateRequest, TipoEscena } from '../models/actriz';

@Injectable({
  providedIn: 'root'
})
export class EscenaService {
  private readonly baseUrl = 'https://demo-ia-production.up.railway.app/api/escenas'; // Usar URL completa del backend

  constructor(private http: HttpClient) {}

  // Obtener todas las escenas
  getEscenas(): Observable<Escena[]> {
    return this.http.get<Escena[]>(this.baseUrl);
  }

  // Obtener escena por ID
  getEscenaById(id: number): Observable<Escena> {
    return this.http.get<Escena>(`${this.baseUrl}/${id}`);
  }

  // Crear escena
  createEscena(escena: EscenaCreateRequest): Observable<Escena> {
    return this.http.post<Escena>(this.baseUrl, escena);
  }

  // Crear escena con imagen
  createEscenaConImagen(escena: EscenaCreateRequest, imagen: File): Observable<Escena> {
    const formData = new FormData();

    // Convertir duración al formato ISO 8601 Duration
    const formatDuration = (minutes: string | number | undefined): string => {
      if (!minutes) return 'PT0M'; // Duración por defecto si no se proporciona
      const mins = typeof minutes === 'string' ? parseInt(minutes) : minutes;
      return `PT${mins}M`; // Formato ISO 8601: PT30M para 30 minutos
    };

    // Asegurar que los IDs sean números y las actrices tengan el formato correcto
    const escenaFormatted = {
      ...escena,
      tipoEscenaId: Number(escena.tipoEscenaId),
      duracion: formatDuration(escena.duracion), // Convertir duración al formato correcto
      actrices: escena.actrices?.map(actriz => ({
        actrizId: Number(actriz.actrizId),
        papel: actriz.papel || ''
      }))
    };

    console.log('Datos que se van a enviar al backend:', JSON.stringify(escenaFormatted, null, 2));
    console.log('Imagen que se va a enviar:', imagen.name, imagen.size, 'bytes');

    formData.append('escena', JSON.stringify(escenaFormatted));
    formData.append('imagen', imagen);

    console.log('FormData creado, haciendo petición a:', `${this.baseUrl}/con-imagen`);

    return this.http.post<Escena>(`${this.baseUrl}/con-imagen`, formData);
  }

  // Actualizar escena
  updateEscena(id: number, escena: EscenaCreateRequest): Observable<Escena> {
    return this.http.put<Escena>(`${this.baseUrl}/${id}`, escena);
  }

  // Actualizar escena con imagen
  updateEscenaConImagen(id: number, escena: EscenaCreateRequest, imagen?: File): Observable<Escena> {
    const formData = new FormData();

    // Convertir duración al formato ISO 8601 Duration
    const formatDuration = (minutes: string | number | undefined): string => {
      if (!minutes) return 'PT0M'; // Duración por defecto si no se proporciona
      const mins = typeof minutes === 'string' ? parseInt(minutes) : minutes;
      return `PT${mins}M`; // Formato ISO 8601: PT30M para 30 minutos
    };

    // Asegurar que los IDs sean números y las actrices tengan el formato correcto
    const escenaFormatted = {
      ...escena,
      tipoEscenaId: Number(escena.tipoEscenaId),
      duracion: formatDuration(escena.duracion), // Convertir duración al formato correcto
      actrices: escena.actrices?.map(actriz => ({
        actrizId: Number(actriz.actrizId),
        papel: actriz.papel || ''
      }))
    };

    console.log('Actualizando escena ID:', id);
    console.log('Datos que se van a enviar al backend:', JSON.stringify(escenaFormatted, null, 2));

    formData.append('escena', JSON.stringify(escenaFormatted));

    if (imagen) {
      console.log('Nueva imagen que se va a enviar:', imagen.name, imagen.size, 'bytes');
      formData.append('imagen', imagen);
    } else {
      console.log('No se proporciona nueva imagen, se mantendrá la existente');
    }

    console.log('FormData creado, haciendo petición a:', `${this.baseUrl}/${id}/con-imagen`);

    return this.http.put<Escena>(`${this.baseUrl}/${id}/con-imagen`, formData);
  }

  // Eliminar escena
  deleteEscena(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Obtener escenas por actriz
  getEscenasPorActriz(actrizId: number): Observable<Escena[]> {
    return this.http.get<Escena[]>(`${this.baseUrl}/actriz/${actrizId}`);
  }

  // Obtener tipos de escena
  getTiposEscena(): Observable<TipoEscena[]> {
    return this.http.get<TipoEscena[]>('https://demo-ia-production.up.railway.app/api/tipos-escena');
  }
}
