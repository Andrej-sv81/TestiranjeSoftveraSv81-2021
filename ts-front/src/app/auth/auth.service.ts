import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tap, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient, private router: Router) {}

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && !!window.sessionStorage;
  }

  login(email: string, password: string): Observable<any> {
    const authHeader = 'Basic ' + btoa(`${email}:${password}`);
    const headers = new HttpHeaders({ Authorization: authHeader });

    return this.http.get<any>(`${this.baseUrl}/login`, { headers })
      .pipe(
        tap(user => {
          if (this.isBrowser()) {
            sessionStorage.setItem('user', JSON.stringify(user));
            sessionStorage.setItem('authToken', authHeader);
          }
        })
      );
  }

  logout(): void {
    if (this.isBrowser()) {
      sessionStorage.removeItem('user');
      sessionStorage.removeItem('authToken');
    }
    this.router.navigate(['/login']);
  }

  getUser(): any {
    if (!this.isBrowser()) return null;
    const user = sessionStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  isLoggedIn(): boolean {
    return this.isBrowser() && !!sessionStorage.getItem('authToken');
  }

  getAuthToken(): string {
    return this.isBrowser() ? (sessionStorage.getItem('authToken') || '') : '';
  }
}
