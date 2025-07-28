import { Routes } from '@angular/router';
import { ActrizListComponent } from './components/actriz-list/actriz-list';
import { ActrizFormComponent } from './components/actriz-form/actriz-form';
import { ActrizDetailComponent } from './components/actriz-detail/actriz-detail';
import { EscenaListComponent } from './components/escena-list/escena-list';
import { EscenaFormComponent } from './components/escena-form/escena-form';
import { EscenaDetailComponent } from './components/escena-detail/escena-detail';
import { TipoEscenaListComponent } from './components/tipo-escena-list/tipo-escena-list';
import { TipoEscenaFormComponent } from './components/tipo-escena-form/tipo-escena-form';

export const routes: Routes = [
  { path: '', redirectTo: '/actrices', pathMatch: 'full' },

  // Rutas de actrices
  { path: 'actrices', component: ActrizListComponent },
  { path: 'actrices/crear', component: ActrizFormComponent },
  { path: 'actrices/:id', component: ActrizDetailComponent },
  { path: 'actrices/:id/editar', component: ActrizFormComponent },

  // Rutas de escenas
  { path: 'escenas', component: EscenaListComponent },
  { path: 'escenas/crear', component: EscenaFormComponent },
  { path: 'escenas/:id', component: EscenaDetailComponent },
  { path: 'escenas/:id/editar', component: EscenaFormComponent },

  // Rutas de tipos de escena
  { path: 'tipos-escena', component: TipoEscenaListComponent },
  { path: 'tipos-escena/crear', component: TipoEscenaFormComponent },
  { path: 'tipos-escena/:id/editar', component: TipoEscenaFormComponent },

  { path: '**', redirectTo: '/actrices' }
];
