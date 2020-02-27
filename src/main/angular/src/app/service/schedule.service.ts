import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Schedule} from '../model/Schedule';
import {PoolActivity} from '../model/PoolActivity';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }


  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/schedules');
  }

  getById(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/schedules/' + id);
  }

  save(schedule: Schedule): Observable<any> {
    const poolId = schedule.pool;
    schedule.pool = null;
    return this.authenticationService.postRequest('/api/pools/' + poolId + '/schedules', schedule);
  }

  assignAPoolActivityToSchedule(id: number, activity: PoolActivity): Observable<any> {
    // TODO: gestire la persistenza di staff.
    return this.authenticationService.postRequest('/api/schedules/' + id + '/assign-activity', activity);
  }

  delete(schedule: Schedule): Observable<any> {
    return this.authenticationService.deleteRequest('/api/schedules/' + schedule.id);
  }

  update(id: number, schedule: Schedule): Observable<any> {
    return this.authenticationService.putRequest('/api/schedules/' + id, schedule);
  }

  getActivities(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/schedules/' + id + '/activities');
  }
}
