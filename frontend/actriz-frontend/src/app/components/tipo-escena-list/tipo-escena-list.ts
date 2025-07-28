import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TipoEscenaService } from '../../services/tipo-escena.service';
import { TipoEscena } from '../../models/tipo-escena';

@Component({
  selector: 'app-tipo-escena-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './tipo-escena-list.html',
  styleUrl: './tipo-escena-list.scss'
})
export class TipoEscenaListComponent implements OnInit {
  tiposEscena: TipoEscena[] = [];
  loading = false;
  error: string | null = null;

  constructor(private tipoEscenaService: TipoEscenaService) {}

  ngOnInit(): void {
    this.loadTiposEscena();
  }

  loadTiposEscena(): void {
    this.loading = true;
    this.tipoEscenaService.getTiposEscena().subscribe({
      next: (tipos) => {
        this.tiposEscena = tipos;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los tipos de escena';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  deleteTipoEscena(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este tipo de escena?')) {
      this.tipoEscenaService.deleteTipoEscena(id).subscribe({
        next: () => {
          this.loadTiposEscena(); // Recargar la lista
        },
        error: (err) => {
          this.error = 'Error al eliminar el tipo de escena';
          console.error('Error:', err);
        }
      });
    }
  }
}
