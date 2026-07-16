import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ThemeService } from '../../core/services/theme.service';
import { EtudiantService } from '../../core/services/etudiant.service';
import { EncadreurService } from '../../core/services/encadreur.service';
import { Etudiant, Encadreur, StatutTheme } from '../../core/models/types';
import { ToastrService } from 'ngx-toastr';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-theme-form',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './theme-form.component.html',
  styleUrls: ['./theme-form.component.css']
})
export class ThemeFormComponent implements OnInit {
  themeForm!: FormGroup;
  isEditMode = false;
  themeId?: number;
  submitting = false;

  // Listes pour les sélections
  etudiants: Etudiant[] = [];
  encadreurs: Encadreur[] = [];
  statuts = Object.values(StatutTheme);

  constructor(
    private fb: FormBuilder,
    private themeService: ThemeService,
    private etudiantService: EtudiantService,
    private encadreurService: EncadreurService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadDropdownData();
  }

  initForm(): void {
    this.themeForm = this.fb.group({
      titre: ['', [Validators.required]],
      description: ['', [Validators.required]],
      domaine: ['', [Validators.required]],
      statut: [StatutTheme.EN_ATTENTE, [Validators.required]],
      etudiantId: ['', [Validators.required]],
      encadreurId: ['', [Validators.required]]
    });
  }

  loadDropdownData(): void {
    this.submitting = true;
    forkJoin({
      etudiants: this.etudiantService.getAll(),
      encadreurs: this.encadreurService.getAll()
    }).subscribe({
      next: (res) => {
        this.etudiants = res.etudiants;
        this.encadreurs = res.encadreurs;
        this.submitting = false;

        // Une fois les listes chargées, on vérifie si on est en mode édition
        this.route.params.subscribe(params => {
          if (params['id']) {
            this.isEditMode = true;
            this.themeId = +params['id'];
            this.loadTheme(this.themeId);
          }
        });
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Impossible de charger les listes d\'étudiants ou d\'encadreurs', 'Erreur');
        this.submitting = false;
        this.router.navigate(['/themes']);
      }
    });
  }

  loadTheme(id: number): void {
    this.themeService.getById(id).subscribe({
      next: (theme) => {
        // Mappe les données vers le formulaire
        this.themeForm.patchValue({
          titre: theme.titre,
          description: theme.description,
          domaine: theme.domaine,
          statut: theme.statut,
          etudiantId: theme.etudiantId,
          encadreurId: theme.encadreurId
        });
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Impossible de charger le thème', 'Erreur');
        this.router.navigate(['/themes']);
      }
    });
  }

  onSubmit(): void {
    if (this.themeForm.invalid) {
      this.themeForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const themeRequest = {
      titre: this.themeForm.value.titre,
      description: this.themeForm.value.description,
      domaine: this.themeForm.value.domaine,
      statut: this.themeForm.value.statut,
      etudiantId: +this.themeForm.value.etudiantId,
      encadreurId: +this.themeForm.value.encadreurId
    };

    if (this.isEditMode && this.themeId) {
      this.themeService.update(this.themeId, themeRequest).subscribe({
        next: () => {
          this.toastr.success('Thème mis à jour avec succès');
          this.router.navigate(['/themes']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la mise à jour', 'Erreur');
          this.submitting = false;
        }
      });
    } else {
      this.themeService.create(themeRequest).subscribe({
        next: () => {
          this.toastr.success('Thème créé avec succès');
          this.router.navigate(['/themes']);
        },
        error: (err) => {
          console.error(err);
          this.toastr.error(err.error?.message || 'Erreur lors de la création', 'Erreur');
          this.submitting = false;
        }
      });
    }
  }

  getStatusLabel(statut: string): string {
    switch (statut) {
      case 'VALIDE': return 'Validé';
      case 'REJETE': return 'Rejeté';
      case 'SOUTENU': return 'Soutenu';
      case 'EN_ATTENTE':
      default:
        return 'En attente';
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.themeForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
}
