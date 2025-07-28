import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EscenaService } from '../../services/escena.service';
import { Escena } from '../../models/actriz';

@Component({
  selector: 'app-escena-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './escena-list.html',
  styleUrl: './escena-list.scss'
})
export class EscenaListComponent implements OnInit {
  escenas: Escena[] = [];
  loading = false;
  error: string | null = null;

  constructor(private escenaService: EscenaService) {}

  ngOnInit(): void {
    this.loadEscenas();
  }

  loadEscenas(): void {
    this.loading = true;
    this.escenaService.getEscenas().subscribe({
      next: (escenas) => {
        this.escenas = escenas;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar las escenas';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  deleteEscena(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta escena?')) {
      this.escenaService.deleteEscena(id).subscribe({
        next: () => {
          this.loadEscenas(); // Recargar la lista
        },
        error: (err) => {
          this.error = 'Error al eliminar la escena';
          console.error('Error:', err);
        }
      });
    }
  }

  onImageError(event: any): void {
    // Reemplazar imagen rota con placeholder
    event.target.src = 'assets/images/no-image.svg';
    event.target.alt = 'Imagen no disponible';
  }

  // Función para convertir duración ISO 8601 a formato legible
  formatDuration(duration: string | undefined): string {
    if (!duration) return 'No especificada';

    // Extraer minutos del formato PT30M
    const match = duration.match(/PT(\d+)M/);
    if (match) {
      const minutes = parseInt(match[1]);
      if (minutes === 1) {
        return '1 minuto';
      } else {
        return `${minutes} minutos`;
      }
    }

    // Si no coincide con el formato esperado, devolver el valor original
    return duration;
  }
}
