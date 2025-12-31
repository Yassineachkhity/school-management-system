import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate, CanActivateChild {
  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const roles = (route.data['roles'] as string[] | undefined) ?? [];
    if (!roles.length || this.auth.hasAnyRole(roles)) {
      return true;
    }
    return this.router.parseUrl('/forbidden');
  }

  canActivateChild(route: ActivatedRouteSnapshot): boolean | UrlTree {
    return this.canActivate(route);
  }
}
