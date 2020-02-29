import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/User';
import {Router} from '@angular/router';
import {JwtHelperService} from '@auth0/angular-jwt';
import {StaffRole} from '../model/StaffRole';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  user: User;
  helper = new JwtHelperService();
  header = new HttpHeaders();
  token: string = null;

  constructor(private http: HttpClient, private router: Router) {
  }

  isStaffLogged(): boolean {
    if (this.token == null) {
      this.token = localStorage.getItem('token');
      if (this.token == null) {
        return false;
      }
      if (this.helper.isTokenExpired(this.token)) {
        console.log('token expired');
        this.logout();
        this.redirect('/login');
        return false;
      }
      this.setVariables(this.token);
    }
    return true;
  }

  isLogged(): boolean {
    return this.token != null;
  }

  login(token: string) {
    this.setVariables(token);
    localStorage.setItem('token', token);
    this.router.navigate(['/']);
  }

  private setVariables(token: string) {
    this.token = token;
    this.user = new User();
    this.user.id = this.helper.decodeToken(token).id;
    this.user.role = this.helper.decodeToken(token).role;
  }

  logout() {
    localStorage.removeItem('token');
    this.token = null;
    this.user = null;
  }

  redirect(path: string) {
    this.router.navigate([path]);
  }

  loginRequest(parameters: any): Observable<any> {
    const head = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post('/login', parameters, {headers: head, responseType: 'text'});
  }

  registerRequest(parameters: any, registrationToken: string): Observable<any> {
    let head = new HttpHeaders().set('Content-Type', 'application/json');
    let path: string;
    if (registrationToken == null) {
      head = head.set('Authorization', 'Bearer ' + this.token);
      path = '/register';
    } else {
      path = '/register?token=' + registrationToken;
    }
    return this.http.post(path, parameters, {
      headers: head,
      responseType: 'text'
    });
  }

  getRequest(path: string): Observable<any> {
    if (path.includes('api', 0) && path.includes('staff', 0)) {
      // console.log('auth required', this.token);
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
    }
    return this.http.get(path, {headers: this.header});
  }

  postRequest(path: string, object: any) {
    if (this.isStaffLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.post(path, object, {headers: this.header});
    }
    console.error('no logged user!');
    return null;

  }

  deleteRequest(path: string) {
    if (this.isStaffLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.delete(path, {headers: this.header});
    }
    console.error('no logged user!');
    return null;
  }

  putRequest(path: string, parameters: any) {
    if (this.isStaffLogged()) {
      this.header = this.header.set('Authorization', 'Bearer ' + this.token);
      return this.http.put(path, parameters, {headers: this.header});
    }
    console.error('no logged user!');
    return null;
  }


  isAdmin(): boolean {
    return this.isLogged() && this.user.role == StaffRole.ADMIN;
  }

  isManager(): boolean {
    return this.isLogged() && this.user.role == StaffRole.MANAGER;
  }

  isWorker(): boolean {
    return this.isLogged() && this.user.role == StaffRole.WORKER;
  }
}
