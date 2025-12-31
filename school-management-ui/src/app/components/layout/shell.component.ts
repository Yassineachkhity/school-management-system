import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService, AuthUser } from '../../core/auth/auth.service';

interface NavItem {
  label: string;
  route: string;
  roles?: string[];
  badge?: string;
}

interface NavSection {
  title: string;
  items: NavItem[];
}

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
  styleUrl: './shell.component.scss'
})
export class ShellComponent {
  readonly user$: Observable<AuthUser | null>;

  readonly sections: NavSection[] = [
    {
      title: 'Core',
      items: [
        { label: 'Dashboard', route: '/dashboard' }
      ]
    },
    {
      title: 'Academics',
      items: [
        { label: 'Students', route: '/students', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
        { label: 'Teachers', route: '/teachers', roles: ['ADMIN', 'TEACHER'] },
        { label: 'Departements', route: '/departements', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
        { label: 'Courses', route: '/courses', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
        { label: 'Sections', route: '/sections', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
        { label: 'Inscriptions', route: '/inscriptions', roles: ['ADMIN', 'TEACHER', 'STUDENT'] }
      ]
    },
    {
      title: 'Admin',
      items: [
        { label: 'Users', route: '/admin/users', roles: ['ADMIN'], badge: 'Admin' },
        { label: 'Roles', route: '/admin/roles', roles: ['ADMIN'] },
        { label: 'User Roles', route: '/admin/user-roles', roles: ['ADMIN'] }
      ]
    }
  ];

  constructor(private readonly auth: AuthService) {
    this.user$ = this.auth.userChanges();
  }

  canShow(item: NavItem): boolean {
    return !item.roles || this.auth.hasAnyRole(item.roles);
  }

  sectionHasItems(section: NavSection): boolean {
    return section.items.some(item => this.canShow(item));
  }

  async logout(): Promise<void> {
    await this.auth.logout();
  }
}
