import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly baseUrl = environment.apiBaseUrl.replace(/\/$/, '');

  constructor(private readonly http: HttpClient) {}

  list<T>(path: string, params?: HttpParams | Record<string, string | number>): Observable<T[]> {
    return this.http.get<T[]>(this.buildUrl(path), { params });
  }

  get<T>(path: string, params?: HttpParams | Record<string, string | number>): Observable<T> {
    return this.http.get<T>(this.buildUrl(path), { params });
  }

  post<T>(path: string, body: unknown): Observable<T> {
    return this.http.post<T>(this.buildUrl(path), body);
  }

  patch<T>(path: string, body: unknown): Observable<T> {
    return this.http.patch<T>(this.buildUrl(path), body);
  }

  delete<T>(path: string, body?: unknown): Observable<T> {
    const options = body ? { body } : {};
    return this.http.delete<T>(this.buildUrl(path), options);
  }

  private buildUrl(path: string): string {
    if (path.startsWith('http')) {
      return path;
    }
    const normalized = path.startsWith('/') ? path : `/${path}`;
    return `${this.baseUrl}${normalized}`;
  }
}
