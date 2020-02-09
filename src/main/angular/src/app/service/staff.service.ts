import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Staff} from "../model/Staff";
import {Pool} from "../model/Pool";

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
    return this.http.get('/staff/${id}');
  }

  addStaff(staff: Staff): Observable<any> {
    return this.http.post('/staff', staff);
  }

  assignPoolToStaff(id: number, pool: Pool): Observable<any> {
    //TODO: gestire la persistenza di pool
    return this.http.post('/staff/${id}/assign-pool', pool);
  }

  public assignStaffToStaff(id: number, staff: Staff): Observable<any> {
    //TODO: gestire la persistenza di staff
    return this.http.post('/staff/${id}/assign-staff', staff);
  }

  delete(staff: Staff): Observable<any> {
    return this.http.delete('/staff/${staff.id}');
  }

  update(staff: Staff): Observable<any> {
    return this.http.put('/staff/${staff.id}', staff);
  }
}
