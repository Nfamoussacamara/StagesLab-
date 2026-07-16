import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge px-3 py-2 rounded-pill fs-7 fw-semibold shadow-xs" [ngClass]="getBadgeClass()">
      <i class="bi" [ngClass]="getIconClass()"></i> {{ getLabel() }}
    </span>
  `,
  styles: [`
    .fs-7 {
      font-size: 0.8rem;
    }
    .badge-ambre {
      background-color: #fef3c7;
      color: #d97706;
      border: 1px solid #fde68a;
    }
    .badge-vert {
      background-color: #dcfce7;
      color: #15803d;
      border: 1px solid #bbf7d0;
    }
    .badge-rouge {
      background-color: #fee2e2;
      color: #b91c1c;
      border: 1px solid #fecaca;
    }
    .badge-bleu {
      background-color: #f3e8ff;
      color: #7e22ce;
      border: 1px solid #e9d5ff;
    }
  `]
})
export class StatusBadgeComponent {
  @Input() statut!: string;

  getBadgeClass(): string {
    switch (this.statut) {
      case 'EN_ATTENTE':
        return 'badge-ambre';
      case 'VALIDE':
        return 'badge-vert';
      case 'REJETE':
        return 'badge-rouge';
      case 'SOUTENU':
        return 'badge-bleu';
      default:
        return 'bg-secondary text-white';
    }
  }

  getIconClass(): string {
    switch (this.statut) {
      case 'EN_ATTENTE':
        return 'bi-hourglass-split me-1';
      case 'VALIDE':
        return 'bi-check-circle-fill me-1';
      case 'REJETE':
        return 'bi-x-circle-fill me-1';
      case 'SOUTENU':
        return 'bi-mortarboard-fill me-1';
      default:
        return 'bi-question-circle me-1';
    }
  }

  getLabel(): string {
    switch (this.statut) {
      case 'EN_ATTENTE':
        return 'En attente';
      case 'VALIDE':
        return 'Validé';
      case 'REJETE':
        return 'Rejeté';
      case 'SOUTENU':
        return 'Soutenu';
      default:
        return this.statut || 'Inconnu';
    }
  }
}
