import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../service/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    if (this.authenticationService.isLogged() != null) {
      this.authenticationService.redirect('/');
    }
  }

  login() {
    this.authenticationService.login(this.form.value).subscribe(
      token => this.authenticationService.setToken(token),
      error => console.error(error)
    );
  }

  register() {
    this.authenticationService.register(this.form.value).subscribe(
      token => this.authenticationService.setToken(token),
      error => console.error(error)
    );
  }
}
