import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EncadreurService } from '../../core/services/encadreur.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-encadreur-form',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './encadreur-form.component.html',
  styleUrls: ['./encadreur-form.component.css']
})
export class EncadreurFormComponent implements OnInit {
  encadreurForm!: FormGroup;
  isEditMode = false;
  encadreurId?: number;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private encadreurService: EncadreurService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.encadreurId = +params['id'];
        this.loadEncadreur(this.encadreurId);
      }
    });
  }

  initForm(): void {
    this.encadreurForm = this.fb.group({
      nom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      specialite: ['', [Validators.required]],
      disponibilite: [3, [Validators.required, Validators.min(0)]]
    });
  }

  loadEncadreur(id: number): void {
    this.encadreurService.getById(id).subscribe({
      next: (encadreur) => {
        this.encadreurForm.patchValue(encadreur);
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Impossible de charger les données de l\'encadreur', 'Erreur');
        this.router.navigate(['/encadreurs']);
      }
    });
  }

  onSubmit(): void {
    if (this.encadreurForm.invalid) {
      this.encadreurForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const encadreurData = this.encadreurForm.value;

    if (this.isEditMode && this.encadreurId) {
      this.encadreurService.update(this.encadreurId, encadreurData).subscribe({
        next: () => {
          this.toastr.success('Encadreur mis à jour avec succès');
          this.router.navigate(['/encadreurs']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la mise à jour', 'Erreur');
          this.submitting = false;
        }
      });
    } else {
      this.encadreurService.create(encadreurData).subscribe({
        next: () => {
          this.toastr.success('Encadreur créé avec succès');
          this.router.navigate(['/encadreurs']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la création', 'Erreur');
          this.submitting = false;
        }
      });
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.encadreurForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
}
