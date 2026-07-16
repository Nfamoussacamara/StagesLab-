import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="navbar navbar-expand-lg navbar-light bg-white border-bottom shadow-sm px-4 py-2 sticky-top">
      <div class="container-fluid d-flex justify-content-between align-items-center p-0">
        
        <!-- Toggle button & Current Page Title -->
        <div class="d-flex align-items-center">
          <button type="button" class="btn btn-light rounded-circle me-3 d-flex align-items-center justify-content-center shadow-xs toggle-btn"
                  (click)="onToggle()">
            <i class="bi bi-list fs-4 text-secondary"></i>
          </button>
          
          <h1 class="h4 mb-0 text-dark fw-bold current-title">{{ title }}</h1>
        </div>
        
        <!-- Right side tools (Quick info) -->
        <div class="d-flex align-items-center ms-auto">
          <img src="Logo_univ_labe.png" alt="Université de Labé" class="topbar-logo" style="width: auto; object-fit: contain;">
        </div>
        
      </div>
    </header>
  `,
  styles: [`
    header {
      height: 64px;
      z-index: 999;
    }
    .toggle-btn {
      width: 40px;
      height: 40px;
      transition: background-color 0.2s, transform 0.2s;
    }
    .toggle-btn:hover {
      background-color: #f3f4f6;
      transform: scale(1.05);
    }
    .current-title {
      letter-spacing: -0.3px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      max-width: 200px;
    }
    .topbar-logo {
      height: 44px;
    }
    .fs-7 {
      font-size: 0.85rem;
    }
    @media (max-width: 575.98px) {
      .topbar-logo {
        height: 30px;
      }
      .current-title {
        max-width: 120px;
        font-size: 1rem;
      }
    }
  `]
})
export class TopbarComponent {
  @Input() title = 'LabéStages';
  @Output() toggleSidebar = new EventEmitter<void>();

  onToggle() {
    this.toggleSidebar.emit();
  }
}
