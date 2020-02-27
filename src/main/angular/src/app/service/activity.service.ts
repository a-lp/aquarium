import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PoolActivity} from '../model/PoolActivity';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/activities');
  }

  getById(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/activities/' + id);
  }

  save(activity: PoolActivity): Observable<any> {
    const resp = activity.staffList;
    const scheduleId = activity.schedule;
    activity.schedule = null;
    activity.staffList = [];
    return this.authenticationService.postRequest('/api/schedule/' + scheduleId + '/activities/staff/' + resp, activity);
  }

  delete(activity: PoolActivity): Observable<any> {
    return this.authenticationService.deleteRequest('/api/activities/' + activity.id);
  }

  update(id: number, activity: PoolActivity): Observable<any> {
    return this.authenticationService.putRequest('/api/activities/' + id, activity);
  }


}
