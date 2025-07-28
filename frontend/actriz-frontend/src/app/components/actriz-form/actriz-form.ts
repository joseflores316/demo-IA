import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ActrizService } from '../../services/actriz.service';
import { Actriz, ActrizCreateRequest, Pais } from '../../models/actriz';

@Component({
  selector: 'app-actriz-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './actriz-form.html',
  styleUrl: './actriz-form.scss'
})
export class ActrizFormComponent implements OnInit {
  actrizForm: FormGroup;
  paises: Pais[] = [];
  isEditMode = false;
  actrizId: number | null = null;
  loading = false;
  error: string | null = null;
  selectedFile: File | null = null;
  imagePreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private actrizService: ActrizService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.actrizForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      fechaNacimiento: ['', Validators.required],
      calificacion: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      paisId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadPaises();
    this.checkEditMode();
  }

  checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.actrizId = +id;
      this.loadActriz();
    }
  }

  loadPaises(): void {
    this.actrizService.getPaises().subscribe({
      next: (paises) => {
        this.paises = paises;
      },
      error: (err) => {
        console.error('Error al cargar países:', err);
      }
    });
  }

  loadActriz(): void {
    if (this.actrizId) {
      this.actrizService.getActrizById(this.actrizId).subscribe({
        next: (actriz) => {
          this.actrizForm.patchValue({
            nombre: actriz.nombre,
            fechaNacimiento: actriz.fechaNacimiento,
            calificacion: actriz.calificacion,
            paisId: actriz.paisId || actriz.pais?.id
          });

          if (actriz.imagenUrl) {
            this.imagePreview = actriz.imagenUrl; // La URL ya viene completa
          }
        },
        error: (err) => {
          this.error = 'Error al cargar la actriz';
          console.error('Error:', err);
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;

      // Crear preview de la imagen
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.actrizForm.valid) {
      this.loading = true;
      this.error = null;

      const actrizData: ActrizCreateRequest = this.actrizForm.value;

      if (this.isEditMode && this.actrizId) {
        // Modo edición - usar el endpoint con imagen para preservar la imagen existente
        this.actrizService.updateActrizConImagen(this.actrizId, actrizData, this.selectedFile || undefined).subscribe({
          next: () => {
            this.router.navigate(['/actrices']);
          },
          error: (err) => {
            this.error = 'Error al actualizar la actriz';
            this.loading = false;
            console.error('Error:', err);
          }
        });
      } else {
        // Modo creación
        if (this.selectedFile) {
          this.actrizService.createActrizConImagen(actrizData, this.selectedFile).subscribe({
            next: () => {
              this.router.navigate(['/actrices']);
            },
            error: (err) => {
              this.error = 'Error al crear la actriz con imagen';
              this.loading = false;
              console.error('Error:', err);
            }
          });
        } else {
          this.actrizService.createActriz(actrizData).subscribe({
            next: () => {
              this.router.navigate(['/actrices']);
            },
            error: (err) => {
              this.error = 'Error al crear la actriz';
              this.loading = false;
              console.error('Error:', err);
            }
          });
        }
      }
    } else {
      this.markFormGroupTouched();
    }
  }

  markFormGroupTouched(): void {
    Object.keys(this.actrizForm.controls).forEach(key => {
      const control = this.actrizForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.actrizForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.actrizForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `El campo ${fieldName} es requerido`;
      if (field.errors['maxlength']) return `El campo ${fieldName} es demasiado largo`;
      if (field.errors['min']) return `La calificación debe ser al menos 1`;
      if (field.errors['max']) return `La calificación no puede ser mayor a 5`;
    }
    return '';
  }

  cancel(): void {
    this.router.navigate(['/actrices']);
  }
}
