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
    return this.http.get('/staff');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/staff/' + id);
  }

  addStaff(staff: Staff): Observable<any> {
    return this.http.post('/staff', staff);
  }

  delete(staff: Staff): Observable<any> {
    return this.http.delete('/staff/' + staff.id);
  }

  update(staff: Staff): Observable<any> {
    return this.http.put('/staff/' + staff.id, staff);
  }
}
