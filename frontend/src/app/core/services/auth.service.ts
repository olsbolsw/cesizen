import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, AuthUser, LoginRequest, RegisterRequest } from '../models/auth.model';
import { Role } from '../models/role.enum';

const STORAGE_KEY = 'cesizen_auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly currentUserSignal = signal<AuthUser | null>(this.loadFromStorage());

  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.currentUser() !== null);
  readonly isAdmin = computed(() => this.currentUser()?.role === Role.ADMIN);

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {}

  login(request: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap((response) => this.persistSession(response)),
    );
  }

  register(request: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap((response) => this.persistSession(response)),
    );
  }

  logout(): void {
    this.currentUserSignal.set(null);
    localStorage.removeItem(STORAGE_KEY);
    void this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return this.currentUser()?.token ?? null;
  }

  private persistSession(response: AuthResponse): void {
    const user: AuthUser = {
      userId: response.userId,
      email: response.email,
      firstName: response.firstName,
      lastName: response.lastName,
      role: response.role,
      token: response.accessToken,
    };
    this.currentUserSignal.set(user);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
  }

  private loadFromStorage(): AuthUser | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
