import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PoolActivity} from '../model/PoolActivity';
import {Staff} from '../model/Staff';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/activities');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/activities/${id}');
  }

  save(activity: PoolActivity): Observable<any> {
    return this.http.post('/activities', activity);
  }

  assignStaffToPoolActivity(id: number, staff: Staff): Observable<any> {
    //TODO: gestire la persistenza di staff.
    return this.http.post('/activities/${id}/assign-staff', staff);
  }

  delete(activity: PoolActivity): Observable<any> {
    return this.http.delete('/activities/${activity.id}');
  }

  update(activity: PoolActivity): Observable<any> {
    return this.http.put('/activities/${activity.id}', activity);
  }


}
