import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EtudiantService } from '../../core/services/etudiant.service';
import { Etudiant } from '../../core/models/types';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDeleteComponent } from '../../shared/components/confirm-delete/confirm-delete.component';

@Component({
  selector: 'app-etudiant-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ConfirmDeleteComponent],
  templateUrl: './etudiant-list.component.html',
  styleUrls: ['./etudiant-list.component.css']
})
export class EtudiantListComponent implements OnInit {
  etudiants: Etudiant[] = [];
  searchTerm = '';
  loading = false;

  // Modale de confirmation
  showDeleteModal = false;
  etudiantToDelete?: Etudiant;

  constructor(
    private etudiantService: EtudiantService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEtudiants();
  }

  loadEtudiants(): void {
    this.loading = true;
    this.etudiantService.getAll(this.searchTerm).subscribe({
      next: (data) => {
        this.etudiants = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
        this.toastr.error('Erreur de chargement des étudiants', 'Erreur');
      }
    });
  }

  onSearch(): void {
    this.loadEtudiants();
  }

  triggerDelete(etudiant: Etudiant): void {
    this.etudiantToDelete = etudiant;
    this.showDeleteModal = true;
  }

  confirmDelete(): void {
    if (this.etudiantToDelete?.id) {
      this.etudiantService.delete(this.etudiantToDelete.id).subscribe({
        next: () => {
          this.toastr.success('Étudiant supprimé avec succès');
          this.showDeleteModal = false;
          this.etudiantToDelete = undefined;
          this.loadEtudiants();
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
    this.etudiantToDelete = undefined;
  }
}
