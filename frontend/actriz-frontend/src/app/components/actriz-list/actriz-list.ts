import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ActrizService } from '../../services/actriz.service';
import { Actriz } from '../../models/actriz';

@Component({
  selector: 'app-actriz-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './actriz-list.html',
  styleUrl: './actriz-list.scss'
})
export class ActrizListComponent implements OnInit {
  actrices: Actriz[] = [];
  loading = false;
  error: string | null = null;

  constructor(private actrizService: ActrizService) {}

  ngOnInit(): void {
    this.loadActrices();
  }

  loadActrices(): void {
    this.loading = true;
    this.error = null;

    this.actrizService.getActrices().subscribe({
      next: (actrices) => {
        this.actrices = actrices;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar las actrices';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  deleteActriz(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta actriz?')) {
      this.actrizService.deleteActriz(id).subscribe({
        next: () => {
          this.loadActrices(); // Recargar la lista
        },
        error: (err) => {
          this.error = 'Error al eliminar la actriz';
          console.error('Error:', err);
        }
      });
    }
  }

  getImageUrl(imagenUrl: string | undefined): string {
    if (!imagenUrl) {
      return 'assets/images/no-image.svg'; // Imagen por defecto SVG
    }
    // La URL ya viene completa desde el backend
    return imagenUrl;
  }
}
