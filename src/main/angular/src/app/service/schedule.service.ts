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
    return this.http.get('/');
  }

  getById(id: number): Observable<any> {
    return this.http.get('/schedules/${id}');
  }

  save(schedule: Schedule): Observable<any> {
    return this.http.post('/schedules', schedule);
  }

  assignAPoolActivityToSchedule(id: number, activity: PoolActivity): Observable<any> {
    // TODO: gestire la persistenza di staff.
    return this.http.post('/schedules/${id}/assign-activity', activity);
  }

  delete(schedule: Schedule): Observable<any> {
    return this.http.delete('/schedules/${schedule.id}');
  }

  update(schedule: Schedule): Observable<any> {
    return this.http.put('/schedules/${schedule.id}', schedule);
  }

}
