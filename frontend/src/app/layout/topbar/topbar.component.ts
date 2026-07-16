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
        <div class="d-flex align-items-center text-muted fs-7">
          <span class="d-none d-sm-inline me-2 fw-semibold text-secondary">Université de Labé</span>
          <span class="d-none d-sm-inline badge bg-primary-subtle text-primary border border-primary-subtle px-2.5 py-1">Licence 3</span>
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
    }
    .fs-7 {
      font-size: 0.85rem;
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
