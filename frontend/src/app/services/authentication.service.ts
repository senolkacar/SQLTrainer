import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { User } from '../models/user';
import { plainToClass } from 'class-transformer';
import { Observable } from 'rxjs';
import { Role } from '../models/user';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {

    // l'utilisateur couramment connecté (undefined sinon)
    public currentUser?: User;

    constructor(private http: HttpClient, @Inject('BASE_URL') private baseUrl: string) {
        // au départ on récupère un éventuel utilisateur stocké dans le sessionStorage
        let data = sessionStorage.getItem('currentUser');
        if (data)
            data = JSON.parse(data);
        this.currentUser = plainToClass(User, data);
    }

    login(pseudo: string, password: string) : Observable<User> {
        return this.http.post<any>(`${this.baseUrl}api/users/authenticate`, { pseudo, password })
            .pipe(map(user => {
                user = plainToClass(User, user);
                // login successful if there's a jwt token in the response
                if (typeof user.role === 'string') {
                    if (user.role === 'Teacher') user.role = Role.Teacher;
                    else if (user.role === 'Student') user.role = Role.Student;
                }
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    sessionStorage.setItem('currentUser', JSON.stringify(user));
                    this.currentUser = user;
                }

                return user;
            }));
    }

    logout() {
        // remove user from local storage to log user out
        sessionStorage.removeItem('currentUser');
        this.currentUser = undefined;
    }
}