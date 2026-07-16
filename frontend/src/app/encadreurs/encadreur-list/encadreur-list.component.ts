import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EncadreurService } from '../../core/services/encadreur.service';
import { Encadreur } from '../../core/models/types';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDeleteComponent } from '../../shared/components/confirm-delete/confirm-delete.component';

@Component({
  selector: 'app-encadreur-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ConfirmDeleteComponent],
  templateUrl: './encadreur-list.component.html',
  styleUrls: ['./encadreur-list.component.css']
})
export class EncadreurListComponent implements OnInit {
  encadreurs: Encadreur[] = [];
  loading = false;

  // Modale de confirmation
  showDeleteModal = false;
  encadreurToDelete?: Encadreur;

  constructor(
    private encadreurService: EncadreurService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEncadreurs();
  }

  loadEncadreurs(): void {
    this.loading = true;
    this.encadreurService.getAll().subscribe({
      next: (data) => {
        this.encadreurs = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
        this.toastr.error('Erreur lors du chargement des encadreurs', 'Erreur');
      }
    });
  }

  triggerDelete(encadreur: Encadreur): void {
    this.encadreurToDelete = encadreur;
    this.showDeleteModal = true;
  }

  confirmDelete(): void {
    if (this.encadreurToDelete?.id) {
      this.encadreurService.delete(this.encadreurToDelete.id).subscribe({
        next: () => {
          this.toastr.success('Encadreur supprimé avec succès');
          this.showDeleteModal = false;
          this.encadreurToDelete = undefined;
          this.loadEncadreurs();
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
    this.encadreurToDelete = undefined;
  }
}
