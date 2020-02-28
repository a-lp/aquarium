import {Injectable} from '@angular/core';
import {Pool} from '../model/Pool';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class PoolService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  save(pool: Pool): Observable<any> {
    const sectorId = pool.sector;
    const responsible = pool.responsible;
    pool.responsible = null;
    pool.sector = null;
    return this.authenticationService.postRequest('/api/sectors/' + sectorId + '/responsible/' + responsible + '/pools', pool);
  }

  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/pools');
  }

  getAllByResponsible(): Observable<any> {
    if (this.authenticationService.isAdmin()) {
      return this.getAll();
    }
    if (!this.authenticationService.isLogged()) {
      return new Observable<any>(null);
    } else {
      return this.authenticationService.getRequest('/api/staff/' + this.authenticationService.user.id + '/pools');
    }
  }

  getPool(id: string): Observable<any> {
    return this.authenticationService.getRequest('/api/pools/' + id);
  }

  remove(pool: Pool): Observable<any> {
    return this.authenticationService.deleteRequest('/api/pools/' + pool.id);
  }

  update(id: number, pool: Pool): Observable<any> {
    return this.authenticationService.putRequest('/api/pools/' + id, pool);
  }

  getFishes(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/pools/' + id + '/fishes');
  }

  getSchedules(id: number): Observable<any> {
    return this.authenticationService.getRequest('/api/pools/' + id + '/schedules');
  }
}
