import { Component, OnInit } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth/auth.service';

interface StatCard {
  label: string;
  path: string;
  roles: string[];
  count$: Observable<number | null>;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  stats: StatCard[] = [];

  constructor(private readonly api: ApiService, private readonly auth: AuthService) {}

  ngOnInit(): void {
    const baseStats = [
      { label: 'Students', path: '/api/students', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
      { label: 'Teachers', path: '/api/teachers', roles: ['ADMIN', 'TEACHER'] },
      { label: 'Departements', path: '/api/departements', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
      { label: 'Courses', path: '/api/courses', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
      { label: 'Sections', path: '/api/sections', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
      { label: 'Inscriptions', path: '/api/inscriptions', roles: ['ADMIN', 'TEACHER', 'STUDENT'] }
    ];

    this.stats = baseStats
      .filter(stat => this.auth.hasAnyRole(stat.roles))
      .map(stat => ({
        ...stat,
        count$: this.api.list<unknown>(stat.path).pipe(
          map(items => items.length),
          catchError(() => of(null))
        )
      }));
  }
}
