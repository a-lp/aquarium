import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../service/authentication.service';
import {StaffRole} from '../model/StaffRole';
import {StaffService} from '../service/staff.service';

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
    token: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    birthday: new FormControl('', Validators.required),
    socialSecurity: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
  });
  roles: Array<StaffRole> = Object.values(StaffRole);

  constructor(private authenticationService: AuthenticationService, private staffService: StaffService) {
  }

  ngOnInit() {
    if (this.authenticationService.isStaffLogged()) {
      this.authenticationService.redirect('/');
    }
  }

  login() {
    const credentials = Object.assign({}, {
      username: this.formLogin.value.username,
      password: this.formLogin.value.password,
    })
    this.authenticationService.loginRequest(credentials).subscribe(
      token => this.authenticationService.login(token),
      error => {
        this.error = error.error;
      }
    );
  }

  register() {
    this.registration = !this.registration;
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
    const firstToken = this.form.value.token;
    delete this.form.value.token;
    this.formLogin.addControl('profile', this.form);
    this.authenticationService.registerRequest(this.formLogin.value, firstToken).subscribe(
      token => this.authenticationService.login(token),
      error => {
        this.error = error.error;
      }
    );
  }
}
