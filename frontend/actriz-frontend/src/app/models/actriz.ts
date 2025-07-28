export interface Actriz {
  id?: number;
  nombre: string;
  fechaNacimiento: string;
  calificacion: number;
  fechaRegistro?: string;
  pais?: Pais;
  paisId?: number;
  imagenUrl?: string;
  caracteristicas?: Caracteristica[];
  escenas?: Escena[];
}

export interface Pais {
  id: number;
  nombre: string;
  codigoIso?: string;
  banderaUrl?: string;
}

export interface Caracteristica {
  id: number;
  nombre: string;
  descripcion?: string;
  tipo: TipoCaracteristica;
}

export interface Escena {
  id: number;
  descripcion?: string;
  fechaGrabacion?: string;
  duracion?: string;
  ubicacion?: string;
  imagenUrl?: string; // Añadiendo la propiedad imagenUrl
  tipoEscena?: TipoEscena;
  papel?: string; // Este campo se usa cuando la escena viene desde una actriz
  actrices?: ActrizEscenaInfo[]; // Lista de actrices asociadas a la escena
}

export interface TipoEscena {
  id: number;
  nombre: string;
}

export enum TipoCaracteristica {
  FISICA = 'FISICA',
  ESTILO = 'ESTILO',
  PARTICULARIDAD = 'PARTICULARIDAD'
}

export interface ActrizCreateRequest {
  nombre: string;
  fechaNacimiento: string;
  calificacion: number;
  paisId: number;
  imagenUrl?: string;
}

export interface EscenaCreateRequest {
  descripcion?: string;
  fechaGrabacion?: string;
  duracion?: string;
  ubicacion?: string;
  tipoEscenaId: number;
  imagenUrl?: string; // Añadiendo imagenUrl para consistencia
  actrices?: ActrizEscenaInfo[];
}

export interface ActrizEscenaInfo {
  actrizId: number;
  papel?: string;
}
