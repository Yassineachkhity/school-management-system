import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../core/api.service';
import { OptionItem } from '../../core/models';

interface UserDTO {
  userId: string;
  email: string;
  createdAt?: string;
  lastLoginAt?: string;
}

@Component({
  selector: 'app-users-admin',
  templateUrl: './users-admin.component.html',
  styleUrl: './users-admin.component.scss'
})
export class UsersAdminComponent implements OnInit {
  users: UserDTO[] = [];
  selected: UserDTO | null = null;
  loading = false;
  errorMessage = '';
  syncing = false;

  readonly roleOptions: OptionItem[] = [
    { value: 'ADMIN', label: 'Admin' },
    { value: 'TEACHER', label: 'Teacher' },
    { value: 'STUDENT', label: 'Student' }
  ];

  createForm: UntypedFormGroup;
  updateForm: UntypedFormGroup;
  passwordForm: UntypedFormGroup;

  constructor(private readonly api: ApiService, private readonly fb: UntypedFormBuilder) {
    this.createForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      roleLabel: ['', Validators.required]
    });

    this.updateForm = this.fb.group({
      email: ['', [Validators.email]]
    });

    this.passwordForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.api.list<UserDTO>('/api/users').pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to load users.';
        this.loading = false;
        return of([]);
      })
    ).subscribe(users => {
      this.users = users;
      this.loading = false;
    });
  }

  selectUser(user: UserDTO): void {
    this.selected = user;
    this.updateForm.patchValue({ email: user.email });
    this.passwordForm.reset();
  }

  createUser(): void {
    if (this.createForm.invalid) {
      return;
    }
    this.errorMessage = '';
    const createPayload = {
      email: String(this.createForm.value.email || '').trim(),
      password: String(this.createForm.value.password || ''),
      roleLabel: this.createForm.value.roleLabel
    };
    this.api.post<UserDTO>('/api/users', createPayload).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to create user.';
        return of(null);
      })
    ).subscribe(result => {
      if (result) {
        this.createForm.reset();
        this.loadUsers();
      }
    });
  }

  updateUser(): void {
    if (!this.selected || this.updateForm.invalid) {
      return;
    }
    const email = String(this.updateForm.value.email || '').trim();
    if (!email) {
      return;
    }
    this.errorMessage = '';
    this.api.patch<UserDTO>(`/api/users/${this.selected.userId}`, { email }).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to update user.';
        return of(null);
      })
    ).subscribe(result => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  updatePassword(): void {
    if (!this.selected || this.passwordForm.invalid) {
      return;
    }
    this.errorMessage = '';
    const password = String(this.passwordForm.value.password || '');
    this.api.patch<void>(`/api/users/${this.selected.userId}/password`, { password }).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to update password.';
        return of(null);
      })
    ).subscribe(result => {
      if (result !== null) {
        this.passwordForm.reset();
      }
    });
  }

  deleteUser(user: UserDTO): void {
    const confirmed = confirm('Delete this user?');
    if (!confirmed) {
      return;
    }
    this.errorMessage = '';
    this.api.delete<void>(`/api/users/${user.userId}`).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to delete user.';
        return of(null);
      })
    ).subscribe(() => {
      if (this.selected?.userId === user.userId) {
        this.selected = null;
      }
      this.loadUsers();
    });
  }

  syncUsers(): void {
    this.syncing = true;
    this.errorMessage = '';
    this.api.post<UserDTO[]>('/api/users/sync', {}).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Sync failed.';
        this.syncing = false;
        return of(null);
      })
    ).subscribe(() => {
      this.syncing = false;
      this.loadUsers();
    });
  }

  formatDate(value?: string): string {
    if (!value) {
      return '-';
    }
    return value.replace('T', ' ');
  }
}
