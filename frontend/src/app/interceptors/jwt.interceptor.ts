import { inject } from '@angular/core';
import { HttpRequest, HttpHandlerFn, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';

export const jwtInterceptor: HttpInterceptorFn = (request: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
    const authenticationService = inject(AuthenticationService);
    const router = inject(Router);

    // add authorization header with jwt token if available
    let currentUser = authenticationService.currentUser;
    if (currentUser && currentUser.token) {
        request = addToken(request, currentUser.token);
    }

    return next(request).pipe(
        catchError(err => {
            if (err.status === 401 && err.headers.get("token-expired")) {
                return handle401Error(authenticationService, router);
            } else {
                return throwError(err);
            }
        })
    );
};

function handle401Error(authenticationService: AuthenticationService, router: Router): Observable<HttpEvent<any>> {
    authenticationService.logout();
    router.navigateByUrl("/login");
    return EMPTY;   // Just emits 'complete', and nothing else.
}

function addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
        setHeaders: {
            'Authorization': `Bearer ${token}`
        }
    });
}
