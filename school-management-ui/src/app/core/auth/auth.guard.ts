import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  canActivate(_: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    if (this.auth.isAuthenticated()) {
      return true;
    }
    return this.router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    });
  }

  canActivateChild(_: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    return this.canActivate(_, state);
  }
}
