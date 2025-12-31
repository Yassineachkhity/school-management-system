import Keycloak, { KeycloakProfile } from 'keycloak-js';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthUser {
  username?: string;
  email?: string;
  fullName?: string;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly keycloak = new Keycloak({
    url: environment.keycloak.url,
    realm: environment.keycloak.realm,
    clientId: environment.keycloak.clientId
  });

  private readonly authenticatedSubject = new BehaviorSubject<boolean>(false);
  private readonly userSubject = new BehaviorSubject<AuthUser | null>(null);

  init(): Promise<boolean> {
    return this.keycloak
      .init({
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
        checkLoginIframe: false
      })
      .then(authenticated => {
        this.authenticatedSubject.next(!!authenticated);
        if (authenticated) {
          return this.loadUser();
        }
        this.userSubject.next(null);
        return false;
      })
      .catch(() => {
        this.authenticatedSubject.next(false);
        this.userSubject.next(null);
        return false;
      });
  }

  login(redirectUri?: string): Promise<void> {
    return this.keycloak.login({
      redirectUri: redirectUri ?? `${window.location.origin}/dashboard`
    });
  }

  logout(): Promise<void> {
    return this.keycloak.logout({
      redirectUri: `${window.location.origin}/login`
    });
  }

  isAuthenticated(): boolean {
    return this.authenticatedSubject.value;
  }

  authenticatedChanges(): Observable<boolean> {
    return this.authenticatedSubject.asObservable();
  }

  userChanges(): Observable<AuthUser | null> {
    return this.userSubject.asObservable();
  }

  getUser(): AuthUser | null {
    return this.userSubject.value;
  }

  hasAnyRole(roles: string[] = []): boolean {
    if (!roles.length) {
      return true;
    }
    const userRoles = this.userSubject.value?.roles ?? [];
    return roles.some(role => userRoles.includes(role.toUpperCase()));
  }

  async getToken(): Promise<string | null> {
    if (!this.keycloak.authenticated) {
      return null;
    }
    try {
      await this.keycloak.updateToken(30);
      return this.keycloak.token ?? null;
    } catch {
      return this.keycloak.token ?? null;
    }
  }

  private async loadUser(): Promise<boolean> {
    const profile = await this.keycloak.loadUserProfile().catch(() => null);
    const parsed = this.keycloak.tokenParsed as {
      preferred_username?: string;
      email?: string;
      name?: string;
      realm_access?: { roles?: string[] };
    } | null;
    const roles = (parsed?.realm_access?.roles ?? []).map(role =>
      role.toUpperCase().replace(/^ROLE_/, '')
    );

    const user = this.buildUser(profile, parsed, roles);
    this.userSubject.next(user);
    return true;
  }

  private buildUser(
    profile: KeycloakProfile | null,
    parsed: { preferred_username?: string; email?: string; name?: string } | null,
    roles: string[]
  ): AuthUser {
    const fullName = profile?.firstName && profile?.lastName
      ? `${profile.firstName} ${profile.lastName}`
      : profile?.firstName ?? parsed?.name ?? parsed?.preferred_username;

    return {
      username: profile?.username ?? parsed?.preferred_username,
      email: profile?.email ?? parsed?.email,
      fullName,
      roles
    };
  }
}
