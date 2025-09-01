import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './auth.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  if (req.url.includes('/login') || req.url.includes('/registration')) {
    return next(req);
  }

  const authHeader = authService.getAuthToken();
  if (authHeader) {
    const cloned = req.clone({
      setHeaders: { Authorization: authHeader }
    });
    return next(cloned);
  }

  return next(req);
};
