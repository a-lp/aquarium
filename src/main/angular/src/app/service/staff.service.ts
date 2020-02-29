import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Staff} from '../model/Staff';
import {AuthenticationService} from './authentication.service';
import {StaffRole} from "../model/StaffRole";

@Injectable({
  providedIn: 'root'
})
export class StaffService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/staff');
  }

  getById(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/staff/' + id);
  }

  addStaff(staff: Staff): Observable<any> {
    return this.authenticationService.postRequest('/api/staff', staff);
  }

  delete(staff: Staff): Observable<any> {
    return this.authenticationService.deleteRequest('/api/staff/' + staff.id);
  }

  update(id: number, staff: Staff): Observable<any> {
    return this.authenticationService.putRequest('/api/staff/' + id, staff);
  }

  getPools(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/staff/' + id + '/pools');
  }

  getSectors(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/staff/' + id + '/sectors');
  }

  getActivities(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/staff/' + id + '/activities');
  }

  getByRole(role: StaffRole) {
    return this.authenticationService.getRequest('/api/staff/role/' + role);
  }

  getBySchedulesFromPoolSector(id: number) {
    return this.authenticationService.getRequest('/api/schedules/' + id + '/staff');
  }
}
