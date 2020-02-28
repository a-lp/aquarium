import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../service/authentication.service';
import {StaffRole} from '../model/StaffRole';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  registration = false;
  error = '';
  formLogin = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    birthday: new FormControl('', Validators.required),
    socialSecurity: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
  });
  roles: Array<StaffRole> = Object.values(StaffRole);

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    if (this.authenticationService.isStaffLogged()) {
      this.authenticationService.redirect('/');
    }
  }

  login() {
    this.authenticationService.loginRequest(this.formLogin.value).subscribe(
      token => this.authenticationService.login(token),
      error => {
        this.error = error.error;
      }
    );
  }

  register() {
    this.registration = true;
  }

  isRegistration() {
    return this.registration;
  }


  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1)
      + '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }

  save() {
    this.formLogin.addControl('profile', this.form);
    this.authenticationService.registerRequest(this.formLogin.value).subscribe(
      token => this.authenticationService.login(token),
      error => this.error = error.error
    );
  }
}
