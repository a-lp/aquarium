import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from "../model/User";
import {Router} from "@angular/router";
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  user: User = new User();
  helper = new JwtHelperService();

  constructor(private http: HttpClient, private router: Router) {
  }

  isLogged(): any {
    const token = localStorage.getItem('token');
    if (token != null) {
      const decodedToken = this.helper.decodeToken(token);
      console.log(decodedToken);
    }
    return token;
  }

  login(parameters: any): Observable<any> {
    const head = new HttpHeaders().set('Content-Type', 'application/json')
    return this.http.post('/login', parameters, {headers: head, responseType: 'text'});
  }

  register(parameters: any): Observable<any> {
    const head = new HttpHeaders().set('Content-Type', 'application/json')
    return this.http.post('/register', parameters, {headers: head, responseType: 'text'});
  }

  setToken(token: string) {
    this.user.token = token;
    localStorage.setItem('token', token);
    this.router.navigate(['/']);
  }

  logout() {
    localStorage.removeItem('token');
  }

  redirect(path: string) {
    this.router.navigate([path]);
  }
}
