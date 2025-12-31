import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShellComponent } from './components/layout/shell.component';
import { CrudPageComponent } from './components/crud-page/crud-page.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EntityPageComponent } from './pages/entity-page/entity-page.component';
import { UsersAdminComponent } from './pages/admin/users-admin.component';
import { RolesAdminComponent } from './pages/admin/roles-admin.component';
import { UserRolesAdminComponent } from './pages/admin/user-roles-admin.component';
import { ForbiddenComponent } from './pages/forbidden/forbidden.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AuthService } from './core/auth/auth.service';
import { AuthInterceptor } from './core/auth/auth.interceptor';

export function initializeAuth(auth: AuthService): () => Promise<boolean> {
  return () => auth.init();
}

@NgModule({
  declarations: [
    AppComponent,
    ShellComponent,
    CrudPageComponent,
    LoginComponent,
    DashboardComponent,
    EntityPageComponent,
    UsersAdminComponent,
    RolesAdminComponent,
    UserRolesAdminComponent,
    ForbiddenComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeAuth,
      deps: [AuthService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
