import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ActrizService } from '../../services/actriz.service';
import { EscenaService } from '../../services/escena.service';
import { Actriz, Escena } from '../../models/actriz';

@Component({
  selector: 'app-actriz-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './actriz-detail.html',
  styleUrl: './actriz-detail.scss'
})
export class ActrizDetailComponent implements OnInit {
  actriz: Actriz | null = null;
  escenas: Escena[] = [];
  loading = false;
  error: string | null = null;
  actrizId: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private actrizService: ActrizService,
    private escenaService: EscenaService
  ) {
    this.actrizId = +this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadActriz();
    this.loadEscenas();
  }

  loadActriz(): void {
    this.loading = true;
    this.actrizService.getActrizById(this.actrizId).subscribe({
      next: (actriz) => {
        this.actriz = actriz;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar la actriz';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  loadEscenas(): void {
    this.escenaService.getEscenasPorActriz(this.actrizId).subscribe({
      next: (escenas) => {
        this.escenas = escenas;
      },
      error: (err) => {
        console.error('Error al cargar escenas:', err);
        // No mostramos error aquí ya que es opcional
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/actrices']);
  }

  editActriz(): void {
    this.router.navigate(['/actrices', this.actrizId, 'editar']);
  }

  deleteActriz(): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta actriz?')) {
      this.actrizService.deleteActriz(this.actrizId).subscribe({
        next: () => {
          this.router.navigate(['/actrices']);
        },
        error: (err) => {
          this.error = 'Error al eliminar la actriz';
          console.error('Error:', err);
        }
      });
    }
  }

  deleteEscena(escenaId: number): void {
    if (confirm('¿Estás seguro de que quieres desasociar esta escena?')) {
      // Aquí implementarías la lógica para desasociar la escena de la actriz
      // Por ahora solo recargamos las escenas
      this.loadEscenas();
    }
  }
}
