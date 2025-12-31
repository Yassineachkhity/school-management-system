import { Observable } from 'rxjs';

export type FieldType =
  | 'text'
  | 'email'
  | 'tel'
  | 'number'
  | 'date'
  | 'datetime'
  | 'select'
  | 'textarea';

export interface OptionItem {
  value: string;
  label: string;
}

export interface FieldConfig {
  key: string;
  label: string;
  type: FieldType;
  required?: boolean;
  min?: number;
  max?: number;
  step?: number;
  placeholder?: string;
  hint?: string;
  options?: OptionItem[];
  optionsProvider?: () => Observable<OptionItem[]>;
}

export interface ColumnConfig {
  key: string;
  label: string;
}

export interface CrudPermissions {
  view: string[];
  create?: string[];
  update?: string[];
  delete?: string[];
}

export interface CrudConfig {
  entityKey: string;
  title: string;
  description: string;
  path: string;
  idField: string;
  columns: ColumnConfig[];
  createFields: FieldConfig[];
  updateFields: FieldConfig[];
  permissions: CrudPermissions;
  emptyState?: string;
}
