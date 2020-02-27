import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/User';
import {Router} from '@angular/router';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  user: User = new User();
  helper = new JwtHelperService();
  header = new HttpHeaders();
  token: string = null;

  constructor(private http: HttpClient, private router: Router) {
  }

  isLogged(): boolean {
    if (this.token == null) {
      this.token = localStorage.getItem('token');
    }
    if (this.token == null) {
      return false;
    }
    if (this.helper.isTokenExpired(this.token)) {
      console.log('token expired');
      localStorage.removeItem('token');
      this.token = null;
      this.redirect('/login');
      return false;
    }
    return true;
  }

  login(parameters: any): Observable<any> {
    const head = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post('/login', parameters, {headers: head, responseType: 'text'});
  }

  register(parameters: any): Observable<any> {
    const head = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post('/register', parameters, {headers: head, responseType: 'text'});
  }

  setToken(token: string) {
    this.user.token = token;
    this.token = token;
    localStorage.setItem('token', token);
    this.router.navigate(['/']);
  }

  logout() {
    localStorage.removeItem('token');
    this.token = null;
  }

  redirect(path: string) {
    this.router.navigate([path]);
  }

  getRequest(path: string): Observable<any> {
    if (path.includes('api', 0) && path.includes('staff', 0)) {
      // console.log('auth required', this.token);
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
    }
    return this.http.get(path, {headers: this.header});
  }

  postRequest(path: string, object: any) {
    if (this.isLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.post(path, object, {headers: this.header});
    }
    console.error('no logged user!');
    return null;

  }

  deleteRequest(path: string) {
    if (this.isLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.delete(path, {headers: this.header});
    }
    console.error('no logged user!');
    return null;
  }

  putRequest(path: string, parameters: any) {
    if (this.isLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.put(path, parameters, {headers: this.header});
    }
    console.error('no logged user!');
    return null;
  }
}
