import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { OptionItem } from './models';

interface DepartementItem {
  departementId: string;
  name?: string;
  departementCode?: string;
}

interface TeacherItem {
  teacherId: string;
  firstName?: string;
  lastName?: string;
  employeeNumber?: string;
}

interface CourseItem {
  courseId: string;
  title?: string;
  courseCode?: string;
}

interface SectionItem {
  sectionId: string;
  academicYear?: string;
  room?: string;
}

interface StudentItem {
  studentId: string;
  firstName?: string;
  lastName?: string;
  apogeCode?: string;
}

interface UserItem {
  userId: string;
  email?: string;
}

@Injectable({ providedIn: 'root' })
export class LookupService {
  constructor(private readonly api: ApiService) {}

  getDepartements(): Observable<OptionItem[]> {
    return this.api.list<DepartementItem>('/api/departements').pipe(
      map(items =>
        items.map(item => ({
          value: item.departementId,
          label: this.buildLabel(item.name, item.departementCode)
        }))
      )
    );
  }

  getTeachers(): Observable<OptionItem[]> {
    return this.api.list<TeacherItem>('/api/teachers').pipe(
      map(items =>
        items.map(item => ({
          value: item.teacherId,
          label: this.buildLabel(this.joinName(item.firstName, item.lastName), item.employeeNumber)
        }))
      )
    );
  }

  getCourses(): Observable<OptionItem[]> {
    return this.api.list<CourseItem>('/api/courses').pipe(
      map(items =>
        items.map(item => ({
          value: item.courseId,
          label: this.buildLabel(item.title, item.courseCode)
        }))
      )
    );
  }

  getSections(): Observable<OptionItem[]> {
    return this.api.list<SectionItem>('/api/sections').pipe(
      map(items =>
        items.map(item => ({
          value: item.sectionId,
          label: this.buildLabel(item.academicYear, item.room)
        }))
      )
    );
  }

  getStudents(): Observable<OptionItem[]> {
    return this.api.list<StudentItem>('/api/students').pipe(
      map(items =>
        items.map(item => ({
          value: item.studentId,
          label: this.buildLabel(this.joinName(item.firstName, item.lastName), item.apogeCode)
        }))
      )
    );
  }

  getUsers(): Observable<OptionItem[]> {
    return this.api.list<UserItem>('/api/users').pipe(
      map(items =>
        items.map(item => ({
          value: item.userId,
          label: item.email ?? item.userId
        }))
      )
    );
  }

  private buildLabel(primary?: string, secondary?: string): string {
    const safePrimary = primary?.trim() || 'Unknown';
    const safeSecondary = secondary?.trim();
    return safeSecondary ? `${safePrimary} (${safeSecondary})` : safePrimary;
  }

  private joinName(firstName?: string, lastName?: string): string | undefined {
    const parts = [firstName, lastName].filter(Boolean) as string[];
    return parts.length ? parts.join(' ') : undefined;
  }
}
