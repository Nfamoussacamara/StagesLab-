import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { EtudiantService } from '../../core/services/etudiant.service';
import { Etudiant } from '../../core/models/types';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-etudiant-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './etudiant-detail.component.html',
  styleUrls: ['./etudiant-detail.component.css']
})
export class EtudiantDetailComponent implements OnInit {
  etudiant?: Etudiant;
  loading = true;

  constructor(
    private etudiantService: EtudiantService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadEtudiant(+id);
    } else {
      this.router.navigate(['/etudiants']);
    }
  }

  loadEtudiant(id: number): void {
    this.loading = true;
    this.etudiantService.getById(id).subscribe({
      next: (data) => {
        this.etudiant = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Impossible de charger les détails de l\'étudiant', 'Erreur');
        this.router.navigate(['/etudiants']);
        this.loading = false;
      }
    });
  }
}
