import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../service/authentication.service';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  shown = '';
  error: HttpErrorResponse = null;

  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    if (!this.authenticationService.isStaffLogged()) {
      this.authenticationService.redirect('/login');
    }
    const url = this.router.url.split('/');
    this.shown = url[url.length - 1];
  }

  activateCompoent($event: any) {
    $event.onError.subscribe(
      error => {
        this.error = error;
      }
    );
  }

  deactivateCompoent($event: any) {
    this.error = null;
  }

  closeErrorPage() {
    this.error = null;
  }

}
