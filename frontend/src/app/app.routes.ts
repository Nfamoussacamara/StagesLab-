import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'etudiants',
    children: [
      {
        path: '',
        loadComponent: () => import('./etudiants/etudiant-list/etudiant-list.component').then(m => m.EtudiantListComponent)
      },
      {
        path: 'nouveau',
        loadComponent: () => import('./etudiants/etudiant-form/etudiant-form.component').then(m => m.EtudiantFormComponent)
      },
      {
        path: 'details/:id',
        loadComponent: () => import('./etudiants/etudiant-detail/etudiant-detail.component').then(m => m.EtudiantDetailComponent)
      },
      {
        path: 'modifier/:id',
        loadComponent: () => import('./etudiants/etudiant-form/etudiant-form.component').then(m => m.EtudiantFormComponent)
      }
    ]
  },
  {
    path: 'encadreurs',
    children: [
      {
        path: '',
        loadComponent: () => import('./encadreurs/encadreur-list/encadreur-list.component').then(m => m.EncadreurListComponent)
      },
      {
        path: 'nouveau',
        loadComponent: () => import('./encadreurs/encadreur-form/encadreur-form.component').then(m => m.EncadreurFormComponent)
      },
      {
        path: 'modifier/:id',
        loadComponent: () => import('./encadreurs/encadreur-form/encadreur-form.component').then(m => m.EncadreurFormComponent)
      }
    ]
  },
  {
    path: 'themes',
    children: [
      {
        path: '',
        loadComponent: () => import('./themes/theme-list/theme-list.component').then(m => m.ThemeListComponent)
      },
      {
        path: 'nouveau',
        loadComponent: () => import('./themes/theme-form/theme-form.component').then(m => m.ThemeFormComponent)
      },
      {
        path: 'modifier/:id',
        loadComponent: () => import('./themes/theme-form/theme-form.component').then(m => m.ThemeFormComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
