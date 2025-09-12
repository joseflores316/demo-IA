import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EscenaService } from '../../services/escena.service';
import { ActrizService } from '../../services/actriz.service';
import { Escena, EscenaCreateRequest, TipoEscena, Actriz, ActrizEscenaInfo } from '../../models/actriz';

@Component({
  selector: 'app-escena-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './escena-form.html',
  styleUrl: './escena-form.scss'
})
export class EscenaFormComponent implements OnInit {
  escenaForm: FormGroup;
  tiposEscena: TipoEscena[] = [];
  actrices: Actriz[] = [];
  isEditMode = false;
  escenaId: number | null = null;
  actrizId: number | null = null;
  loading = false;
  error: string | null = null;
  selectedFile: File | null = null;
  imagePreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private escenaService: EscenaService,
    private actrizService: ActrizService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.escenaForm = this.fb.group({
      descripcion: [''],
      fechaGrabacion: [''],
      duracion: [''],
      ubicacion: [''],
      tipoEscenaId: ['', Validators.required],
      actrices: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadTiposEscena();
    this.loadActrices();
    this.checkEditMode();
    this.checkActrizId();
    this.addActrizFormGroup(); // Agregar al menos una actriz por defecto
  }

  get actricesFormArray(): FormArray {
    return this.escenaForm.get('actrices') as FormArray;
  }

  addActrizFormGroup(): void {
    const actrizGroup = this.fb.group({
      actrizId: ['', Validators.required],
      papel: ['']
    });
    this.actricesFormArray.push(actrizGroup);
  }

  removeActrizFormGroup(index: number): void {
    if (this.actricesFormArray.length > 1) {
      this.actricesFormArray.removeAt(index);
    }
  }

  loadActrices(): void {
    this.actrizService.getActrices().subscribe({
      next: (actrices) => {
        this.actrices = actrices;
      },
      error: (err) => {
        console.error('Error al cargar actrices:', err);
      }
    });
  }

  checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.escenaId = +id;
      this.loadEscena();
    }
  }

  checkActrizId(): void {
    // Verificar si viene desde una actriz específica
    const actrizId = this.route.snapshot.queryParamMap.get('actrizId');
    if (actrizId) {
      this.actrizId = +actrizId;
    }
  }

  loadTiposEscena(): void {
    this.escenaService.getTiposEscena().subscribe({
      next: (tipos) => {
        this.tiposEscena = tipos;
      },
      error: (err) => {
        console.error('Error al cargar tipos de escena:', err);
        // Si no hay tipos, crear algunos por defecto para la demo
        this.tiposEscena = [
          { id: 1, nombre: 'Escena Principal' },
          { id: 2, nombre: 'Escena Secundaria' },
          { id: 3, nombre: 'Escena de Acción' },
          { id: 4, nombre: 'Escena Dramática' }
        ];
      }
    });
  }

  loadEscena(): void {
    if (this.escenaId) {
      this.escenaService.getEscenaById(this.escenaId).subscribe({
        next: (escena) => {
          this.escenaForm.patchValue({
            descripcion: escena.descripcion || '',
            fechaGrabacion: escena.fechaGrabacion || '',
            duracion: escena.duracion || '',
            ubicacion: escena.ubicacion || '',
            tipoEscenaId: escena.tipoEscena?.id || ''
          });

          // Limpiar el FormArray antes de cargar nuevas actrices
          while (this.actricesFormArray.length !== 0) {
            this.actricesFormArray.removeAt(0);
          }

          // Cargar actrices si hay en la escena
          if (escena.actrices && escena.actrices.length > 0) {
            escena.actrices.forEach((actrizInfo: ActrizEscenaInfo) => {
              const actrizGroup = this.fb.group({
                actrizId: [actrizInfo.actrizId.toString(), Validators.required],
                papel: [actrizInfo.papel || '']
              });
              this.actricesFormArray.push(actrizGroup);
            });
          } else {
            // Si no hay actrices, agregar al menos una por defecto
            this.addActrizFormGroup();
          }
        },
        error: (err) => {
          this.error = 'Error al cargar la escena';
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

  removeImage(): void {
    this.selectedFile = null;
    this.imagePreview = null;
    // Limpiar el input file
    const fileInput = document.getElementById('imagen') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  onSubmit(): void {
    if (this.escenaForm.valid) {
      this.loading = true;
      this.error = null;

      const formValue = this.escenaForm.value;

      // Filtrar actrices que tienen actrizId seleccionado
      const actricesSeleccionadas = formValue.actrices
        .filter((actriz: any) => actriz.actrizId)
        .map((actriz: any) => ({
          actrizId: parseInt(actriz.actrizId),
          papel: actriz.papel || ''
        }));

      const escenaData: EscenaCreateRequest = {
        descripcion: formValue.descripcion,
        fechaGrabacion: formValue.fechaGrabacion,
        duracion: formValue.duracion,
        ubicacion: formValue.ubicacion,
        tipoEscenaId: parseInt(formValue.tipoEscenaId),
        actrices: actricesSeleccionadas.length > 0 ? actricesSeleccionadas : undefined
      };

      if (this.isEditMode && this.escenaId) {
        // Modo edición - usar endpoint con imagen para preservar o actualizar la imagen
        console.log('Modo edición - Datos de escena a enviar:', escenaData);
        console.log('Archivo seleccionado para actualización:', this.selectedFile);

        this.escenaService.updateEscenaConImagen(this.escenaId, escenaData, this.selectedFile || undefined).subscribe({
          next: () => {
            this.router.navigate(['/escenas']);
          },
          error: (err) => {
            this.error = 'Error al actualizar la escena';
            this.loading = false;
            console.error('Error completo:', err);
            console.error('Status:', err.status);
            console.error('Message:', err.message);
            console.error('Error details:', err.error);
          }
        });
      } else {
        // Modo creación - usar endpoint con imagen si hay archivo seleccionado
        if (this.selectedFile) {
          // Debug: ver qué datos se están enviando
          console.log('Datos de escena a enviar:', escenaData);
          console.log('Archivo seleccionado:', this.selectedFile);

          this.escenaService.createEscenaConImagen(escenaData, this.selectedFile).subscribe({
            next: () => {
              if (this.actrizId) {
                this.router.navigate(['/actrices', this.actrizId]);
              } else {
                this.router.navigate(['/escenas']);
              }
            },
            error: (err) => {
              this.error = 'Error al crear la escena con imagen';
              this.loading = false;
              console.error('Error completo:', err);
              console.error('Status:', err.status);
              console.error('Message:', err.message);
              console.error('Error details:', err.error);
            }
          });
        } else {
          this.escenaService.createEscena(escenaData).subscribe({
            next: () => {
              if (this.actrizId) {
                this.router.navigate(['/actrices', this.actrizId]);
              } else {
                this.router.navigate(['/escenas']);
              }
            },
            error: (err) => {
              this.error = 'Error al crear la escena';
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
    Object.keys(this.escenaForm.controls).forEach(key => {
      const control = this.escenaForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.escenaForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.escenaForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `El campo ${fieldName} es requerido`;
    }
    return '';
  }

  cancel(): void {
    if (this.actrizId) {
      this.router.navigate(['/actrices', this.actrizId]);
    } else {
      this.router.navigate(['/escenas']);
    }
  }
}
