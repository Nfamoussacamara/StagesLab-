import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-confirm-delete',
  standalone: true,
  template: `
    <div class="modal fade show d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.5);">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title fs-5">{{ title }}</h5>
            <button type="button" class="btn-close btn-close-white" (click)="onCancel()"></button>
          </div>
          <div class="modal-body py-4">
            <p class="mb-0 fs-6 text-secondary">{{ message }}</p>
          </div>
          <div class="modal-footer border-top-0 pt-0">
            <button type="button" class="btn btn-light" (click)="onCancel()">Annuler</button>
            <button type="button" class="btn btn-danger" (click)="onConfirm()">Supprimer</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
    }
  `]
})
export class ConfirmDeleteComponent {
  @Input() title = 'Confirmation de suppression';
  @Input() message = 'Êtes-vous sûr de vouloir supprimer cet élément ? Cette action est irréversible.';
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  onConfirm() {
    this.confirmed.emit();
  }

  onCancel() {
    this.cancelled.emit();
  }
}
