import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PoolActivity} from '../model/PoolActivity';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/api/activities');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/api/activities/' + id);
  }

  save(activity: PoolActivity): Observable<any> {
    const resp = activity.staffList;
    const scheduleId = activity.schedule;
    activity.schedule = null;
    activity.staffList = [];
    return this.http.post('/api/schedule/' + scheduleId + '/activities/staff/' + resp, activity);
  }

  delete(activity: PoolActivity): Observable<any> {
    return this.http.delete('/api/activities/' + activity.id);
  }

  update(id: number, activity: PoolActivity): Observable<any> {
    return this.http.put('/api/activities/' + id, activity);
  }


}
