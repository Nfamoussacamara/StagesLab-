import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ThemeService } from '../../core/services/theme.service';
import { Theme, StatutTheme } from '../../core/models/types';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDeleteComponent } from '../../shared/components/confirm-delete/confirm-delete.component';
import { StatusBadgeComponent } from '../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-theme-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ConfirmDeleteComponent, StatusBadgeComponent],
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.css']
})
export class ThemeListComponent implements OnInit {
  themes: Theme[] = [];
  loading = false;

  // Modale de confirmation
  showDeleteModal = false;
  themeToDelete?: Theme;

  constructor(
    private themeService: ThemeService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.loading = true;
    this.themeService.getAll().subscribe({
      next: (data) => {
        this.themes = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
        this.toastr.error('Erreur lors du chargement des thèmes', 'Erreur');
      }
    });
  }

  triggerDelete(theme: Theme): void {
    this.themeToDelete = theme;
    this.showDeleteModal = true;
  }

  confirmDelete(): void {
    if (this.themeToDelete?.id) {
      this.themeService.delete(this.themeToDelete.id).subscribe({
        next: () => {
          this.toastr.success('Thème supprimé avec succès');
          this.showDeleteModal = false;
          this.themeToDelete = undefined;
          this.loadThemes();
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la suppression', 'Erreur');
          this.showDeleteModal = false;
        }
      });
    }
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.themeToDelete = undefined;
  }

  getStatusBadgeClass(statut: StatutTheme): string {
    switch (statut) {
      case StatutTheme.VALIDE:
        return 'bg-success';
      case StatutTheme.REJETE:
        return 'bg-danger';
      case StatutTheme.SOUTENU:
        return 'bg-primary';
      case StatutTheme.EN_ATTENTE:
      default:
        return 'bg-warning text-dark';
    }
  }

  getStatusLabel(statut: StatutTheme): string {
    switch (statut) {
      case StatutTheme.VALIDE:
        return 'Validé';
      case StatutTheme.REJETE:
        return 'Rejeté';
      case StatutTheme.SOUTENU:
        return 'Soutenu';
      case StatutTheme.EN_ATTENTE:
      default:
        return 'En attente';
    }
  }
}
