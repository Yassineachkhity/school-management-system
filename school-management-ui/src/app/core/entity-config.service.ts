import { Injectable } from '@angular/core';
import { CrudConfig, FieldConfig, OptionItem } from './models';
import { LookupService } from './lookup.service';

export type EntityKey =
  | 'students'
  | 'teachers'
  | 'departements'
  | 'courses'
  | 'sections'
  | 'inscriptions';

@Injectable({ providedIn: 'root' })
export class EntityConfigService {
  private readonly configs: Record<EntityKey, CrudConfig>;

  private readonly levelOptions: OptionItem[] = [
    { value: 'ASSISTANT_PROFESSOR', label: 'Assistant professor' },
    { value: 'FULL_TIME_PROFESSOR', label: 'Full time professor' },
    { value: 'PART_TIME_PROFESSOR', label: 'Part time professor' }
  ];

  private readonly teacherStatusOptions: OptionItem[] = [
    { value: 'ACTIVE', label: 'Active' },
    { value: 'INACTIVE', label: 'Inactive' },
    { value: 'ON_LEAVE', label: 'On leave' }
  ];

  private readonly courseStatusOptions: OptionItem[] = [
    { value: 'ACTIVE', label: 'Active' },
    { value: 'INACTIVE', label: 'Inactive' },
    { value: 'ARCHIVED', label: 'Archived' }
  ];

  constructor(private readonly lookup: LookupService) {
    this.configs = this.buildConfigs();
  }

  getConfig(key: EntityKey): CrudConfig {
    return this.configs[key];
  }

  private buildConfigs(): Record<EntityKey, CrudConfig> {
    return {
      students: {
        entityKey: 'students',
        title: 'Students',
        description: 'Manage student records, admissions, and departement assignment.',
        path: '/api/students',
        idField: 'studentId',
        columns: [
          { key: 'apogeCode', label: 'Apoge' },
          { key: 'firstName', label: 'First name' },
          { key: 'lastName', label: 'Last name' },
          { key: 'email', label: 'Email' },
          { key: 'gradeLevel', label: 'Grade' },
          { key: 'departementId', label: 'Departement' }
        ],
        createFields: [
          {
            key: 'userId',
            label: 'User',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getUsers(),
            hint: 'Keycloak user linked to the student'
          },
          { key: 'apogeCode', label: 'Apoge code', type: 'text', required: true },
          { key: 'firstName', label: 'First name', type: 'text', required: true },
          { key: 'lastName', label: 'Last name', type: 'text', required: true },
          { key: 'email', label: 'Email', type: 'email', required: true },
          { key: 'phone', label: 'Phone', type: 'tel' },
          { key: 'admissionDate', label: 'Admission date', type: 'date', required: true },
          { key: 'gradeLevel', label: 'Grade level', type: 'number', required: true, min: 1, max: 5 },
          { key: 'dateOfBirth', label: 'Date of birth', type: 'date', required: true },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getDepartements()
          }
        ],
        updateFields: [
          {
            key: 'userId',
            label: 'User',
            type: 'select',
            optionsProvider: () => this.lookup.getUsers()
          },
          { key: 'apogeCode', label: 'Apoge code', type: 'text' },
          { key: 'firstName', label: 'First name', type: 'text' },
          { key: 'lastName', label: 'Last name', type: 'text' },
          { key: 'email', label: 'Email', type: 'email' },
          { key: 'phone', label: 'Phone', type: 'tel' },
          { key: 'admissionDate', label: 'Admission date', type: 'date' },
          { key: 'gradeLevel', label: 'Grade level', type: 'number', min: 1, max: 5 },
          { key: 'dateOfBirth', label: 'Date of birth', type: 'date' },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            optionsProvider: () => this.lookup.getDepartements()
          }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER', 'STUDENT'],
          create: ['ADMIN'],
          update: ['ADMIN'],
          delete: ['ADMIN']
        },
        emptyState: 'No students yet. Add the first student.'
      },
      teachers: {
        entityKey: 'teachers',
        title: 'Teachers',
        description: 'Track teacher profiles, roles, and departement assignments.',
        path: '/api/teachers',
        idField: 'teacherId',
        columns: [
          { key: 'employeeNumber', label: 'Employee' },
          { key: 'firstName', label: 'First name' },
          { key: 'lastName', label: 'Last name' },
          { key: 'email', label: 'Email' },
          { key: 'level', label: 'Level' },
          { key: 'status', label: 'Status' }
        ],
        createFields: [
          {
            key: 'userId',
            label: 'User',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getUsers()
          },
          { key: 'employeeNumber', label: 'Employee number', type: 'text', required: true },
          { key: 'firstName', label: 'First name', type: 'text', required: true },
          { key: 'lastName', label: 'Last name', type: 'text', required: true },
          { key: 'email', label: 'Email', type: 'email', required: true },
          { key: 'phone', label: 'Phone', type: 'tel' },
          { key: 'address', label: 'Address', type: 'textarea' },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getDepartements()
          },
          {
            key: 'level',
            label: 'Level',
            type: 'select',
            required: true,
            options: this.levelOptions
          },
          { key: 'hireDate', label: 'Hire date', type: 'date' }
        ],
        updateFields: [
          {
            key: 'userId',
            label: 'User',
            type: 'select',
            optionsProvider: () => this.lookup.getUsers()
          },
          { key: 'employeeNumber', label: 'Employee number', type: 'text' },
          { key: 'firstName', label: 'First name', type: 'text' },
          { key: 'lastName', label: 'Last name', type: 'text' },
          { key: 'email', label: 'Email', type: 'email' },
          { key: 'phone', label: 'Phone', type: 'tel' },
          { key: 'address', label: 'Address', type: 'textarea' },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            optionsProvider: () => this.lookup.getDepartements()
          },
          {
            key: 'level',
            label: 'Level',
            type: 'select',
            options: this.levelOptions
          },
          {
            key: 'status',
            label: 'Status',
            type: 'select',
            options: this.teacherStatusOptions
          },
          { key: 'hireDate', label: 'Hire date', type: 'date' }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER'],
          create: ['ADMIN'],
          update: ['ADMIN'],
          delete: ['ADMIN']
        },
        emptyState: 'No teachers yet. Add the first teacher.'
      },
      departements: {
        entityKey: 'departements',
        title: 'Departements',
        description: 'Manage departement catalog and head teacher assignments.',
        path: '/api/departements',
        idField: 'departementId',
        columns: [
          { key: 'departementCode', label: 'Code' },
          { key: 'name', label: 'Name' },
          { key: 'headTeacherId', label: 'Head teacher' }
        ],
        createFields: [
          { key: 'departementCode', label: 'Code', type: 'text', required: true },
          { key: 'name', label: 'Name', type: 'text', required: true },
          { key: 'description', label: 'Description', type: 'textarea' },
          {
            key: 'headTeacherId',
            label: 'Head teacher',
            type: 'select',
            optionsProvider: () => this.lookup.getTeachers()
          }
        ],
        updateFields: [
          { key: 'name', label: 'Name', type: 'text' },
          { key: 'description', label: 'Description', type: 'textarea' },
          {
            key: 'headTeacherId',
            label: 'Head teacher',
            type: 'select',
            optionsProvider: () => this.lookup.getTeachers()
          }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER', 'STUDENT'],
          create: ['ADMIN'],
          update: ['ADMIN'],
          delete: ['ADMIN']
        },
        emptyState: 'No departements yet. Create the first departement.'
      },
      courses: {
        entityKey: 'courses',
        title: 'Courses',
        description: 'Configure course catalog and credit distribution.',
        path: '/api/courses',
        idField: 'courseId',
        columns: [
          { key: 'courseCode', label: 'Code' },
          { key: 'title', label: 'Title' },
          { key: 'creditHours', label: 'Credits' },
          { key: 'semester', label: 'Semester' },
          { key: 'status', label: 'Status' }
        ],
        createFields: [
          { key: 'courseCode', label: 'Course code', type: 'text', required: true },
          { key: 'title', label: 'Title', type: 'text', required: true },
          { key: 'description', label: 'Description', type: 'textarea' },
          { key: 'creditHours', label: 'Credit hours', type: 'number', required: true, min: 1 },
          { key: 'semester', label: 'Semester', type: 'number', required: true, min: 1 },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getDepartements()
          }
        ],
        updateFields: [
          { key: 'title', label: 'Title', type: 'text' },
          { key: 'description', label: 'Description', type: 'textarea' },
          { key: 'creditHours', label: 'Credit hours', type: 'number', min: 1 },
          { key: 'semester', label: 'Semester', type: 'number', min: 1 },
          {
            key: 'departementId',
            label: 'Departement',
            type: 'select',
            optionsProvider: () => this.lookup.getDepartements()
          },
          {
            key: 'status',
            label: 'Status',
            type: 'select',
            options: this.courseStatusOptions
          }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER', 'STUDENT'],
          create: ['ADMIN'],
          update: ['ADMIN'],
          delete: ['ADMIN']
        },
        emptyState: 'No courses yet. Add the first course.'
      },
      sections: {
        entityKey: 'sections',
        title: 'Sections',
        description: 'Set up course sections with teacher assignments.',
        path: '/api/sections',
        idField: 'sectionId',
        columns: [
          { key: 'courseId', label: 'Course' },
          { key: 'teacherId', label: 'Teacher' },
          { key: 'academicYear', label: 'Academic year' },
          { key: 'capacity', label: 'Capacity' },
          { key: 'room', label: 'Room' }
        ],
        createFields: [
          {
            key: 'courseId',
            label: 'Course',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getCourses()
          },
          {
            key: 'teacherId',
            label: 'Teacher',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getTeachers()
          },
          { key: 'academicYear', label: 'Academic year', type: 'text', required: true },
          { key: 'capacity', label: 'Capacity', type: 'number', required: true, min: 1 },
          { key: 'room', label: 'Room', type: 'text', required: true }
        ],
        updateFields: [
          {
            key: 'courseId',
            label: 'Course',
            type: 'select',
            optionsProvider: () => this.lookup.getCourses()
          },
          {
            key: 'teacherId',
            label: 'Teacher',
            type: 'select',
            optionsProvider: () => this.lookup.getTeachers()
          },
          { key: 'academicYear', label: 'Academic year', type: 'text' },
          { key: 'capacity', label: 'Capacity', type: 'number', min: 1 },
          { key: 'room', label: 'Room', type: 'text' }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER', 'STUDENT'],
          create: ['ADMIN'],
          update: ['ADMIN'],
          delete: ['ADMIN']
        },
        emptyState: 'No sections yet. Add the first section.'
      },
      inscriptions: {
        entityKey: 'inscriptions',
        title: 'Inscriptions',
        description: 'Manage student enrollment and grading.',
        path: '/api/inscriptions',
        idField: 'enrollmentId',
        columns: [
          { key: 'studentId', label: 'Student' },
          { key: 'sectionId', label: 'Section' },
          { key: 'enrolledAt', label: 'Enrolled at' },
          { key: 'grade', label: 'Grade' }
        ],
        createFields: [
          {
            key: 'studentId',
            label: 'Student',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getStudents()
          },
          {
            key: 'sectionId',
            label: 'Section',
            type: 'select',
            required: true,
            optionsProvider: () => this.lookup.getSections()
          },
          {
            key: 'enrolledAt',
            label: 'Enrollment time',
            type: 'datetime',
            hint: 'Use local time'
          }
        ],
        updateFields: [
          {
            key: 'grade',
            label: 'Grade',
            type: 'number',
            min: 0,
            max: 20,
            step: 0.5
          }
        ],
        permissions: {
          view: ['ADMIN', 'TEACHER', 'STUDENT'],
          create: ['ADMIN'],
          update: ['ADMIN', 'TEACHER'],
          delete: ['ADMIN']
        },
        emptyState: 'No inscriptions yet. Enroll the first student.'
      }
    };
  }
}
