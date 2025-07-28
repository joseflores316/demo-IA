import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EscenaService } from '../../services/escena.service';
import { Escena } from '../../models/actriz';

@Component({
  selector: 'app-escena-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './escena-detail.html',
  styleUrl: './escena-detail.scss'
})
export class EscenaDetailComponent implements OnInit {
  escena: Escena | null = null;
  loading = false;
  error: string | null = null;
  escenaId: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private escenaService: EscenaService
  ) {
    this.escenaId = +this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadEscena();
  }

  loadEscena(): void {
    this.loading = true;
    this.escenaService.getEscenaById(this.escenaId).subscribe({
      next: (escena) => {
        this.escena = escena;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar la escena';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/escenas']);
  }

  editEscena(): void {
    this.router.navigate(['/escenas', this.escenaId, 'editar']);
  }

  deleteEscena(): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta escena?')) {
      this.escenaService.deleteEscena(this.escenaId).subscribe({
        next: () => {
          this.router.navigate(['/escenas']);
        },
        error: (err) => {
          this.error = 'Error al eliminar la escena';
          console.error('Error:', err);
        }
      });
    }
  }
}
