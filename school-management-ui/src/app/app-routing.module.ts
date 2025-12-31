import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShellComponent } from './components/layout/shell.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EntityPageComponent } from './pages/entity-page/entity-page.component';
import { LoginComponent } from './pages/login/login.component';
import { UsersAdminComponent } from './pages/admin/users-admin.component';
import { RolesAdminComponent } from './pages/admin/roles-admin.component';
import { UserRolesAdminComponent } from './pages/admin/user-roles-admin.component';
import { ForbiddenComponent } from './pages/forbidden/forbidden.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AuthGuard } from './core/auth/auth.guard';
import { RoleGuard } from './core/auth/role.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: ShellComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'students',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT'], entity: 'students' }
      },
      {
        path: 'teachers',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER'], entity: 'teachers' }
      },
      {
        path: 'departements',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT'], entity: 'departements' }
      },
      {
        path: 'courses',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT'], entity: 'courses' }
      },
      {
        path: 'sections',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT'], entity: 'sections' }
      },
      {
        path: 'inscriptions',
        component: EntityPageComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT'], entity: 'inscriptions' }
      },
      {
        path: 'admin/users',
        component: UsersAdminComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] }
      },
      {
        path: 'admin/roles',
        component: RolesAdminComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] }
      },
      {
        path: 'admin/user-roles',
        component: UserRolesAdminComponent,
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] }
      },
      { path: 'forbidden', component: ForbiddenComponent },
      { path: '**', component: NotFoundComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top' })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
