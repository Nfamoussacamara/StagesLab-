import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EtudiantService } from '../../core/services/etudiant.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-etudiant-form',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './etudiant-form.component.html',
  styleUrls: ['./etudiant-form.component.css']
})
export class EtudiantFormComponent implements OnInit {
  etudiantForm!: FormGroup;
  isEditMode = false;
  etudiantId?: number;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private etudiantService: EtudiantService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.etudiantId = +params['id'];
        this.loadEtudiant(this.etudiantId);
      }
    });
  }

  initForm(): void {
    this.etudiantForm = this.fb.group({
      matricule: ['', [Validators.required]],
      nom: ['', [Validators.required]],
      prenom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      niveau: ['L3', [Validators.required]]
    });
  }

  loadEtudiant(id: number): void {
    this.etudiantService.getById(id).subscribe({
      next: (etudiant) => {
        this.etudiantForm.patchValue(etudiant);
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Impossible de charger les données de l\'étudiant', 'Erreur');
        this.router.navigate(['/etudiants']);
      }
    });
  }

  onSubmit(): void {
    if (this.etudiantForm.invalid) {
      this.etudiantForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const etudiantData = this.etudiantForm.value;

    if (this.isEditMode && this.etudiantId) {
      this.etudiantService.update(this.etudiantId, etudiantData).subscribe({
        next: () => {
          this.toastr.success('Étudiant mis à jour avec succès');
          this.router.navigate(['/etudiants']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la mise à jour', 'Erreur');
          this.submitting = false;
        }
      });
    } else {
      this.etudiantService.create(etudiantData).subscribe({
        next: () => {
          this.toastr.success('Étudiant créé avec succès');
          this.router.navigate(['/etudiants']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la création', 'Erreur');
          this.submitting = false;
        }
      });
    }
  }

  // Helpers pour afficher les erreurs de validation
  isFieldInvalid(fieldName: string): boolean {
    const field = this.etudiantForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
}
