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
    return this.http.get('/activities');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/activities/${id}');
  }

  save(activity: PoolActivity): Observable<any> {
    const resp = activity.staffList;
    const scheduleId = activity.schedule;
    activity.schedule = null;
    activity.staffList = [];
    return this.http.post('/schedule/' + scheduleId + '/activities/staff/' + resp.join(','), activity);
  }

  delete(activity: PoolActivity): Observable<any> {
    return this.http.delete('/activities/${activity.id}');
  }

  update(activity: PoolActivity): Observable<any> {
    return this.http.put('/activities/${activity.id}', activity);
  }


}
