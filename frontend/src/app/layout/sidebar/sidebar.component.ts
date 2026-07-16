import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="sidebar d-flex flex-column flex-shrink-0 text-white shadow-lg animate-all"
         [class.collapsed]="isCollapsed">
      
      <!-- Brand Logo / Header -->
      <a routerLink="/dashboard" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none brand-header">
        <span class="brand-icon">🎓</span>
        <span class="fs-4 fw-bold ms-2 brand-logo animate-fade" *ngIf="!isCollapsed">LabéStages</span>
      </a>
      
      <hr class="mx-3 opacity-25">
      
      <!-- Nav Links -->
      <ul class="nav nav-pills flex-column mb-auto px-2">
        <li class="nav-item my-1">
          <a routerLink="/dashboard" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}"
             class="nav-link text-white d-flex align-items-center py-2.5 px-3 rounded-3" title="Tableau de bord">
            <i class="bi bi-grid-1x2-fill fs-5"></i>
            <span class="ms-3 nav-text animate-fade" *ngIf="!isCollapsed">Tableau de bord</span>
          </a>
        </li>
        <li class="nav-item my-1">
          <a routerLink="/etudiants" routerLinkActive="active"
             class="nav-link text-white d-flex align-items-center py-2.5 px-3 rounded-3" title="Étudiants">
            <i class="bi bi-people-fill fs-5"></i>
            <span class="ms-3 nav-text animate-fade" *ngIf="!isCollapsed">Étudiants</span>
          </a>
        </li>
        <li class="nav-item my-1">
          <a routerLink="/encadreurs" routerLinkActive="active"
             class="nav-link text-white d-flex align-items-center py-2.5 px-3 rounded-3" title="Encadreurs">
            <i class="bi bi-person-badge-fill fs-5"></i>
            <span class="ms-3 nav-text animate-fade" *ngIf="!isCollapsed">Encadreurs</span>
          </a>
        </li>
        <li class="nav-item my-1">
          <a routerLink="/themes" routerLinkActive="active"
             class="nav-link text-white d-flex align-items-center py-2.5 px-3 rounded-3" title="Thèmes">
            <i class="bi bi-journal-text fs-5"></i>
            <span class="ms-3 nav-text animate-fade" *ngIf="!isCollapsed">Thèmes</span>
          </a>
        </li>
      </ul>
      
    </div>
  `,
  styles: [`
    :host {
      display: block;
    }
    .sidebar {
      width: 240px;
      height: 100vh;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 1000;
      background-color: #1b7a4e !important; /* Vert émeraude foncé sur la Sidebar */
      transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .sidebar.collapsed {
      width: 70px;
    }
    .brand-header {
      padding: 1.5rem 1rem;
      overflow: hidden;
      white-space: nowrap;
    }
    .brand-icon {
      font-size: 1.6rem;
      min-width: 32px;
      text-align: center;
    }
    .brand-logo {
      letter-spacing: -0.5px;
    }
    .nav-link {
      transition: background-color 0.15s ease, color 0.15s ease;
      white-space: nowrap;
      overflow: hidden;
    }
    .nav-link i {
      min-width: 24px;
      transition: transform 0.2s;
    }
    .nav-link:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }
    .nav-link.active {
      background-color: rgba(255, 255, 255, 0.22) !important;
      color: #ffffff !important;
      font-weight: 700;
      box-shadow: none;
    }
    .nav-link.active i {
      transform: scale(1.1);
    }
    .footer-brand-container {
      border-top: 1px solid rgba(255, 255, 255, 0.08);
    }
    .fs-8 {
      font-size: 0.75rem;
    }
    .animate-all {
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .animate-fade {
      animation: fadeIn 0.2s ease-in-out forwards;
    }
    @keyframes fadeIn {
      from { opacity: 0; transform: translateX(-10px); }
      to { opacity: 1; transform: translateX(0); }
    }
  `]
})
export class SidebarComponent {
  @Input() isCollapsed = false;
}
