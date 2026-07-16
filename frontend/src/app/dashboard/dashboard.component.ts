import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../core/services/dashboard.service';
import { Dashboard } from '../core/models/types';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // Stats initialisées avec des valeurs par défaut pour éviter le blocage
  stats: Dashboard = {
    totalEtudiants: 0,
    totalEncadreurs: 0,
    totalThemes: 0,
    themesEnAttente: 0,
    themesValides: 0,
    themesSoutenus: 0,
    themesRejetes: 0
  };
  loading = true;
  loaded = false;
  error = false;

  constructor(
    private dashboardService: DashboardService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.loading = true;
    this.error = false;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        console.log('Dashboard data received:', data);
        this.stats = data;
        this.loading = false;
        this.loaded = true;
        this.cdr.detectChanges(); // Force la mise à jour de la vue
      },
      error: (err) => {
        console.error('Erreur lors du chargement des statistiques', err);
        this.loading = false;
        this.loaded = true;
        this.error = true;
        this.cdr.detectChanges(); // Force la mise à jour même en cas d'erreur
        this.toastr.error('Impossible de charger les statistiques du tableau de bord', 'Erreur');
      }
    });
  }
}
