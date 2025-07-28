import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TipoEscenaService } from '../../services/tipo-escena.service';
import { TipoEscena, TipoEscenaCreateRequest } from '../../models/tipo-escena';

@Component({
  selector: 'app-tipo-escena-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './tipo-escena-form.html',
  styleUrl: './tipo-escena-form.scss'
})
export class TipoEscenaFormComponent implements OnInit {
  tipoEscenaForm: FormGroup;
  isEditMode = false;
  tipoEscenaId: number | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private tipoEscenaService: TipoEscenaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.tipoEscenaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]]
    });
  }

  ngOnInit(): void {
    this.checkEditMode();
  }

  checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.tipoEscenaId = +id;
      this.loadTipoEscena();
    }
  }

  loadTipoEscena(): void {
    if (this.tipoEscenaId) {
      this.tipoEscenaService.getTipoEscenaById(this.tipoEscenaId).subscribe({
        next: (tipoEscena) => {
          this.tipoEscenaForm.patchValue({
            nombre: tipoEscena.nombre
          });
        },
        error: (err) => {
          this.error = 'Error al cargar el tipo de escena';
          console.error('Error:', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.tipoEscenaForm.valid) {
      this.loading = true;
      this.error = null;

      const tipoEscenaData: TipoEscenaCreateRequest = this.tipoEscenaForm.value;

      if (this.isEditMode && this.tipoEscenaId) {
        // Modo edición
        this.tipoEscenaService.updateTipoEscena(this.tipoEscenaId, tipoEscenaData).subscribe({
          next: () => {
            this.router.navigate(['/tipos-escena']);
          },
          error: (err) => {
            this.error = 'Error al actualizar el tipo de escena';
            this.loading = false;
            console.error('Error:', err);
          }
        });
      } else {
        // Modo creación
        this.tipoEscenaService.createTipoEscena(tipoEscenaData).subscribe({
          next: () => {
            this.router.navigate(['/tipos-escena']);
          },
          error: (err) => {
            this.error = 'Error al crear el tipo de escena';
            this.loading = false;
            console.error('Error:', err);
          }
        });
      }
    } else {
      this.markFormGroupTouched();
    }
  }

  markFormGroupTouched(): void {
    Object.keys(this.tipoEscenaForm.controls).forEach(key => {
      const control = this.tipoEscenaForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.tipoEscenaForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.tipoEscenaForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `El campo ${fieldName} es requerido`;
      if (field.errors['maxlength']) return `El campo ${fieldName} es demasiado largo`;
    }
    return '';
  }

  cancel(): void {
    this.router.navigate(['/tipos-escena']);
  }
}
