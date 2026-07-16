import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, NavigationEnd, ActivatedRoute, RouterModule } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';
import { filter } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule, SidebarComponent, TopbarComponent],
  template: `
    <div class="app-layout" [class.sidebar-collapsed]="isSidebarCollapsed" [class.mobile-sidebar-active]="isMobileActive">
      
      <!-- Sidebar -->
      <app-sidebar [isCollapsed]="isSidebarCollapsed"></app-sidebar>
      
      <!-- Backdrop overlay for mobile sidebar -->
      <div class="mobile-sidebar-overlay" (click)="closeMobileSidebar()" *ngIf="isMobileActive"></div>

      <!-- Main Panel (Topbar + Content Area + Footer) -->
      <div class="main-panel">
        
        <!-- Topbar -->
        <app-topbar [title]="currentPageTitle" (toggleSidebar)="handleSidebarToggle()"></app-topbar>
        
        <!-- Scrolling Content Container -->
        <main class="content-container p-4 flex-grow-1">
          <router-outlet></router-outlet>
        </main>
        
        <!-- Footer -->
        <footer class="bg-white border-top py-3 text-center text-muted fs-8 mt-auto">
          <div class="container-fluid">
            <span>© 2026 Université de Labé — Département Informatique. Tous droits réservés.</span>
          </div>
        </footer>
      </div>
      
    </div>
  `,
  styles: [`
    .app-layout {
      display: flex;
      min-height: 100vh;
      width: 100%;
      overflow-x: hidden;
      background-color: #f8fafc;
    }
    
    .main-panel {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      min-height: 100vh;
      margin-left: 240px;
      width: calc(100% - 240px);
      transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1), width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    
    .sidebar-collapsed .main-panel {
      margin-left: 70px;
      width: calc(100% - 70px);
    }
    
    .content-container {
      background-color: #f8fafc;
      min-height: calc(100vh - 64px - 57px);
      width: 100%;
    }
    
    .fs-8 {
      font-size: 0.8rem;
    }
    
    /* Responsive/Mobile Styles */
    .mobile-sidebar-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(15, 23, 42, 0.4);
      z-index: 998;
      backdrop-filter: blur(2px);
    }

    @media (max-width: 767.98px) {
      .main-panel {
        margin-left: 0 !important;
      }
      
      /* Force sidebar offscreen on mobile */
      ::ng-deep app-sidebar .sidebar {
        transform: translateX(-100%);
      }
      
      /* Slide-in sidebar on mobile when active */
      .mobile-sidebar-active ::ng-deep app-sidebar .sidebar {
        transform: translateX(0);
        width: 240px !important;
      }
      
      /* Hide elements in sidebar unless visible */
      .mobile-sidebar-active ::ng-deep app-sidebar .brand-logo,
      .mobile-sidebar-active ::ng-deep app-sidebar .nav-text,
      .mobile-sidebar-active ::ng-deep app-sidebar .footer-brand-container {
        display: block !important;
      }
    }
  `]
})
export class AppShellComponent implements OnInit, OnDestroy {
  isSidebarCollapsed = false;
  isMobileActive = false;
  currentPageTitle = 'LabéStages';
  private routerSubscription!: Subscription;

  // Simple title mapper matching paths to user-friendly titles
  private routeTitles: { [key: string]: string } = {
    'dashboard': 'Tableau de bord',
    'etudiants': 'Gestion des Étudiants',
    'etudiants/nouveau': 'Nouvel Étudiant',
    'etudiants/details': 'Fiche de l\'Étudiant',
    'etudiants/modifier': 'Modifier l\'Étudiant',
    'encadreurs': 'Gestion des Encadreurs',
    'encadreurs/nouveau': 'Nouvel Encadreur',
    'encadreurs/modifier': 'Modifier l\'Encadreur',
    'themes': 'Gestion des Thèmes & Projets',
    'themes/nouveau': 'Nouveau Thème de Projet',
    'themes/modifier': 'Modifier / Valider le Thème'
  };

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Detect viewport size on load to collapse sidebar by default on tablets
    if (window.innerWidth >= 768 && window.innerWidth <= 1024) {
      this.isSidebarCollapsed = true;
    }

    // Initialize first route title
    this.updateTitleByUrl(this.router.url);

    // Subscribe to router events to sync Topbar title
    this.routerSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.updateTitleByUrl(event.urlAfterRedirects || event.url);
      
      // Auto close mobile sidebar on navigation
      this.isMobileActive = false;
    });
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  handleSidebarToggle(): void {
    if (window.innerWidth < 768) {
      this.isMobileActive = !this.isMobileActive;
    } else {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }
  }

  closeMobileSidebar(): void {
    this.isMobileActive = false;
  }

  private updateTitleByUrl(url: string): void {
    // Clean query parameters and trailing slashes
    const cleanUrl = url.split('?')[0].replace(/^\/|\/$/g, '');
    
    if (!cleanUrl || cleanUrl === '') {
      this.currentPageTitle = 'Tableau de bord';
      return;
    }

    // Direct match
    if (this.routeTitles[cleanUrl]) {
      this.currentPageTitle = this.routeTitles[cleanUrl];
      return;
    }

    // Dynamic segments matching (e.g. details/:id, modifier/:id)
    const segments = cleanUrl.split('/');
    if (segments.length >= 2) {
      // Reconstitute route (e.g. etudiants/details)
      const pattern = `${segments[0]}/${segments[1]}`;
      if (this.routeTitles[pattern]) {
        this.currentPageTitle = this.routeTitles[pattern];
        return;
      }
    }

    // Default fallback
    this.currentPageTitle = this.routeTitles[segments[0]] || 'LabéStages';
  }
}
