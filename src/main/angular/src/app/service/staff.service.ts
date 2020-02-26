import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Staff} from "../model/Staff";

@Injectable({
  providedIn: 'root'
})
export class StaffService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/api/staff');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/api/staff/' + id);
  }

  addStaff(staff: Staff): Observable<any> {
    return this.http.post('/api/staff', staff);
  }

  delete(staff: Staff): Observable<any> {
    return this.http.delete('/api/staff/' + staff.id);
  }

  update(id: number, staff: Staff): Observable<any> {
    return this.http.put('/api/staff/' + id, staff);
  }

  getPools(id: number): Observable<any> {
    return this.http.get('/api/staff/' + id + '/pools');
  }

  getSectors(id: number): Observable<any> {
    return this.http.get('/api/staff/' + id + '/sectors');
  }

  getActivities(id: number): Observable<any> {
    return this.http.get('/api/staff/' + id + '/activities');
  }
}
