import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../service/authentication.service";
import {StaffRole} from "../model/StaffRole";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  registration: boolean = false;
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
    if (this.authenticationService.isLogged() != null) {
      this.authenticationService.redirect('/');
    }
  }

  login() {
    this.authenticationService.login(this.formLogin.value).subscribe(
      token => this.authenticationService.setToken(token),
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
    this.authenticationService.register(this.formLogin.value).subscribe(
      token => this.authenticationService.setToken(token),
      error => this.error = error.error
    );
  }
}
