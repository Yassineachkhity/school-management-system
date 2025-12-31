import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  returnUrl = '/dashboard';
  loading = false;

  constructor(
    private readonly auth: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    const target = this.route.snapshot.queryParamMap.get('returnUrl');
    this.returnUrl = target && target.startsWith('/') ? target : '/dashboard';

    if (this.auth.isAuthenticated()) {
      this.router.navigateByUrl(this.returnUrl);
    }
  }

  async login(): Promise<void> {
    this.loading = true;
    const redirectUri = `${window.location.origin}${this.returnUrl}`;
    await this.auth.login(redirectUri);
  }
}
