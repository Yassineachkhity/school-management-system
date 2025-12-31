import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../core/api.service';
import { OptionItem } from '../../core/models';

interface RoleDTO {
  roleId: string;
  label: string;
}

@Component({
  selector: 'app-roles-admin',
  templateUrl: './roles-admin.component.html',
  styleUrl: './roles-admin.component.scss'
})
export class RolesAdminComponent implements OnInit {
  roles: RoleDTO[] = [];
  loading = false;
  errorMessage = '';

  readonly roleOptions: OptionItem[] = [
    { value: 'ADMIN', label: 'Admin' },
    { value: 'TEACHER', label: 'Teacher' },
    { value: 'STUDENT', label: 'Student' }
  ];

  createForm: UntypedFormGroup;

  constructor(private readonly api: ApiService, private readonly fb: UntypedFormBuilder) {
    this.createForm = this.fb.group({
      label: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(): void {
    this.loading = true;
    this.api.list<RoleDTO>('/api/roles').pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to load roles.';
        this.loading = false;
        return of([]);
      })
    ).subscribe(roles => {
      this.roles = roles;
      this.loading = false;
    });
  }

  createRole(): void {
    if (this.createForm.invalid) {
      return;
    }
    this.errorMessage = '';
    this.api.post<RoleDTO>('/api/roles', this.createForm.value).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to create role.';
        return of(null);
      })
    ).subscribe(result => {
      if (result) {
        this.createForm.reset();
        this.loadRoles();
      }
    });
  }

  deleteRole(role: RoleDTO): void {
    const confirmed = confirm('Delete this role?');
    if (!confirmed) {
      return;
    }
    this.errorMessage = '';
    this.api.delete<void>(`/api/roles/${role.roleId}`).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to delete role.';
        return of(null);
      })
    ).subscribe(() => {
      this.loadRoles();
    });
  }
}
