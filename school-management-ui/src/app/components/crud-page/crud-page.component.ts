import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth/auth.service';
import { CrudConfig, FieldConfig, OptionItem } from '../../core/models';

@Component({
  selector: 'app-crud-page',
  templateUrl: './crud-page.component.html',
  styleUrl: './crud-page.component.scss'
})
export class CrudPageComponent implements OnInit {
  @Input({ required: true }) config!: CrudConfig;

  items: Record<string, unknown>[] = [];
  filteredItems: Record<string, unknown>[] = [];
  fieldOptions: Record<string, OptionItem[]> = {};

  form: UntypedFormGroup;
  mode: 'create' | 'edit' = 'create';
  selected: Record<string, unknown> | null = null;

  filterTerm = '';
  loading = false;
  saving = false;
  errorMessage = '';

  constructor(
    private readonly api: ApiService,
    private readonly auth: AuthService,
    private readonly fb: UntypedFormBuilder
  ) {
    this.form = this.fb.group({});
  }

  ngOnInit(): void {
    this.loadItems();
    this.initializeForm();
    this.loadOptions();
  }

  get canCreate(): boolean {
    return this.hasPermission(this.config.permissions.create);
  }

  get canUpdate(): boolean {
    return this.hasPermission(this.config.permissions.update);
  }

  get canDelete(): boolean {
    return this.hasPermission(this.config.permissions.delete);
  }

  get activeFields(): FieldConfig[] {
    return this.mode === 'create' ? this.config.createFields : this.config.updateFields;
  }

  get showForm(): boolean {
    return this.canCreate || this.canUpdate;
  }

  selectItem(item: Record<string, unknown>): void {
    this.selected = item;
  }

  startCreate(): void {
    if (!this.canCreate) {
      return;
    }
    this.mode = 'create';
    this.selected = null;
    this.form = this.buildForm(this.config.createFields);
  }

  editItem(item: Record<string, unknown>): void {
    if (!this.canUpdate) {
      return;
    }
    this.selected = item;
    this.mode = 'edit';
    this.form = this.buildForm(this.config.updateFields, item);
  }

  resetForm(): void {
    if (this.mode === 'edit' && !this.selected) {
      return;
    }
    if (this.mode === 'edit' && this.selected) {
      this.form = this.buildForm(this.config.updateFields, this.selected);
      return;
    }
    this.form = this.buildForm(this.config.createFields);
  }

  cancelEdit(): void {
    this.selected = null;
    if (this.canCreate) {
      this.mode = 'create';
      this.form = this.buildForm(this.config.createFields);
    }
  }

  submit(): void {
    if (this.form.invalid || this.saving) {
      return;
    }
    if (this.mode === 'edit' && !this.selected) {
      return;
    }

    this.saving = true;
    this.errorMessage = '';

    const payload = this.buildPayload(this.activeFields, this.form.value, this.mode === 'edit');
    const request$ = this.mode === 'create'
      ? this.api.post<Record<string, unknown>>(this.config.path, payload)
      : this.api.patch<Record<string, unknown>>(
          `${this.config.path}/${this.selected?.[this.config.idField]}`,
          payload
        );

    request$.pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Request failed.';
        this.saving = false;
        return of(null);
      })
    ).subscribe(result => {
      if (result) {
        this.loadItems();
        if (this.mode === 'create') {
          this.form.reset();
        }
      }
      this.saving = false;
    });
  }

  deleteItem(item: Record<string, unknown>): void {
    if (!this.canDelete) {
      return;
    }
    const id = item[this.config.idField];
    if (!id) {
      return;
    }
    const confirmed = confirm(`Delete ${this.config.title} entry?`);
    if (!confirmed) {
      return;
    }

    this.api.delete<void>(`${this.config.path}/${id}`).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Delete failed.';
        return of(null);
      })
    ).subscribe(() => {
      this.loadItems();
      if (this.selected && this.selected[this.config.idField] === id) {
        this.cancelEdit();
      }
    });
  }

  applyFilter(): void {
    const term = this.filterTerm.trim().toLowerCase();
    if (!term) {
      this.filteredItems = [...this.items];
      return;
    }
    this.filteredItems = this.items.filter(item =>
      this.config.columns.some(column => {
        const value = item[column.key];
        return String(value ?? '').toLowerCase().includes(term);
      })
    );
  }

  formatValue(value: unknown): string {
    if (value === null || value === undefined || value === '') {
      return '-';
    }
    if (typeof value === 'string') {
      if (value.includes('T')) {
        return value.replace('T', ' ');
      }
      if (value.includes('_')) {
        return value.replace(/_/g, ' ');
      }
      return value;
    }
    return String(value);
  }

  getOptions(field: FieldConfig): OptionItem[] {
    return this.fieldOptions[field.key] ?? field.options ?? [];
  }

  private hasPermission(roles?: string[]): boolean {
    return roles ? this.auth.hasAnyRole(roles) : false;
  }

  private initializeForm(): void {
    if (!this.showForm) {
      return;
    }
    this.mode = this.canCreate ? 'create' : 'edit';
    const fields = this.mode === 'create' ? this.config.createFields : this.config.updateFields;
    this.form = this.buildForm(fields);
  }

  loadItems(): void {
    this.loading = true;
    this.api.list<Record<string, unknown>>(this.config.path).pipe(
      catchError(err => {
        this.errorMessage = err?.error?.message ?? 'Failed to load data.';
        this.loading = false;
        return of([]);
      })
    ).subscribe(items => {
      this.items = items;
      this.filteredItems = [...items];
      this.loading = false;
    });
  }

  private loadOptions(): void {
    if (!this.showForm) {
      return;
    }
    const fields = [...this.config.createFields, ...this.config.updateFields];
    fields.forEach(field => {
      if (this.fieldOptions[field.key]) {
        return;
      }
      if (field.options) {
        this.fieldOptions[field.key] = field.options;
        return;
      }
      if (field.optionsProvider) {
        field.optionsProvider()
          .pipe(catchError(() => of([])))
          .subscribe(options => {
            this.fieldOptions[field.key] = options;
          });
      }
    });
  }

  private buildForm(fields: FieldConfig[], source?: Record<string, unknown> | null): UntypedFormGroup {
    const group: Record<string, unknown> = {};
    fields.forEach(field => {
      const rawValue = source ? source[field.key] : '';
      const initial = this.normalizeForInput(field, rawValue);
      const validators = [];
      if (field.required) {
        validators.push(Validators.required);
      }
      if (field.min !== undefined) {
        validators.push(Validators.min(field.min));
      }
      if (field.max !== undefined) {
        validators.push(Validators.max(field.max));
      }
      if (field.type === 'email') {
        validators.push(Validators.email);
      }
      group[field.key] = [initial ?? '', validators];
    });
    return this.fb.group(group);
  }

  private buildPayload(fields: FieldConfig[], values: Record<string, unknown>, partial: boolean): Record<string, unknown> {
    const payload: Record<string, unknown> = {};
    fields.forEach(field => {
      let value = values[field.key];
      if (partial && (value === null || value === undefined || value === '')) {
        return;
      }
      value = this.normalizeForSubmit(field, value);
      payload[field.key] = value;
    });
    return payload;
  }

  private normalizeForInput(field: FieldConfig, value: unknown): unknown {
    if (value === null || value === undefined) {
      return '';
    }
    if (field.type === 'date' && typeof value === 'string') {
      return value.slice(0, 10);
    }
    if (field.type === 'datetime' && typeof value === 'string') {
      return value.length >= 16 ? value.slice(0, 16) : value;
    }
    return value;
  }

  private normalizeForSubmit(field: FieldConfig, value: unknown): unknown {
    if (field.type === 'number' && value !== null && value !== undefined && value !== '') {
      const parsed = Number(value);
      return Number.isNaN(parsed) ? value : parsed;
    }
    if (field.type === 'datetime' && typeof value === 'string') {
      if (value.length === 16) {
        return `${value}:00`;
      }
    }
    if (typeof value === 'string') {
      return value.trim();
    }
    return value;
  }
}
