import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../core/api.service';
import { LookupService } from '../../core/lookup.service';
import { OptionItem } from '../../core/models';

interface UserRoleDTO {
  userId: string;
  roleId: string;
  roleLabel: string;
}

@Component({
  selector: 'app-user-roles-admin',
  templateUrl: './user-roles-admin.component.html',
  styleUrl: './user-roles-admin.component.scss'
})
export class UserRolesAdminComponent implements OnInit {
  userRoles: UserRoleDTO[] = [];
  userOptions: OptionItem[] = [];
  userMap = new Map<string, string>();
  loading = false;
  errorMessage = '';

  readonly roleOptions: OptionItem[] = [
    { value: 'ADMIN', label: 'Admin' },
    { value: 'TEACHER', label: 'Teacher' },
    { value: 'STUDENT', label: 'Student' }
  ];

  assignForm: UntypedFormGroup;

  constructor(
    private readonly api: ApiService,
    private readonly lookup: LookupService,
    private readonly fb: UntypedFormBuilder
  ) {
    this.assignForm = this.fb.group({
      userId: ['', Validators.required],
      roleLabel: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUserRoles();
    this.lookup.getUsers().pipe(
      catchError(() => of([]))
    ).subscribe(options => {
      this.userOptions = options;
      this.userMap = new Map(options.map(opt => [opt.value, opt.label]));
    });
  }

  loadUserRoles(): void {
    this.loading = true;
    this.api.list<UserRoleDTO>('/api/user-roles').pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to load user roles.';
        this.loading = false;
        return of([]);
      })
    ).subscribe(userRoles => {
      this.userRoles = userRoles;
      this.loading = false;
    });
  }

  assignRole(): void {
    if (this.assignForm.invalid) {
      return;
    }
    this.errorMessage = '';
    this.api.post<UserRoleDTO>('/api/user-roles', this.assignForm.value).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to assign role.';
        return of(null);
      })
    ).subscribe(result => {
      if (result) {
        this.assignForm.reset();
        this.loadUserRoles();
      }
    });
  }

  removeRole(userRole: UserRoleDTO): void {
    const confirmed = confirm('Remove this role assignment?');
    if (!confirmed) {
      return;
    }
    this.errorMessage = '';
    this.api.delete<void>('/api/user-roles', {
      userId: userRole.userId,
      roleLabel: userRole.roleLabel
    }).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to remove role.';
        return of(null);
      })
    ).subscribe(() => {
      this.loadUserRoles();
    });
  }

  resolveUserLabel(userId: string): string {
    return this.userMap.get(userId) ?? userId;
  }
}
