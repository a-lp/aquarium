import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Schedule} from '../model/Schedule';
import {PoolActivity} from '../model/PoolActivity';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(private http: HttpClient) {
  }


  getAll(): Observable<any> {
    return this.http.get('/api/schedules');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/api/schedules/' + id);
  }

  save(schedule: Schedule): Observable<any> {
    const poolId = schedule.pool;
    schedule.pool = null;
    return this.http.post('/api/pools/' + poolId + '/schedules', schedule);
  }

  assignAPoolActivityToSchedule(id: number, activity: PoolActivity): Observable<any> {
    // TODO: gestire la persistenza di staff.
    return this.http.post('/api/schedules/' + id + '/assign-activity', activity);
  }

  delete(schedule: Schedule): Observable<any> {
    return this.http.delete('/api/schedules/' + schedule.id);
  }

  update(id: number, schedule: Schedule): Observable<any> {
    return this.http.put('/api/schedules/' + id, schedule);
  }

  getActivities(id: number): Observable<any> {
    return this.http.get('/api/schedules/' + id + '/activities');
  }
}
