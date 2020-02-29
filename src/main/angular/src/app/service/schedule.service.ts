import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Schedule} from '../model/Schedule';
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
