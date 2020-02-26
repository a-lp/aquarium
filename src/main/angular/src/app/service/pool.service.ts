import {Injectable} from '@angular/core';
import {Pool} from '../model/Pool';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PoolService {

  constructor(private http: HttpClient) {
  }

  save(pool: Pool): Observable<any> {
    const sectorId = pool.sector;
    const responsible = pool.responsible;
    pool.responsible = null;
    pool.sector = null;
    return this.http.post('/api/sectors/' + sectorId + '/responsible/' + responsible + '/pools', pool);
  }

  getAll(): Observable<any> {
    return this.http.get('/api/pools');
  }

  getPool(id: string): Observable<any> {
    return this.http.get('/api/pools/' + id);
  }

  remove(pool: Pool): Observable<any> {
    return this.http.delete('/api/pools/' + pool.id);
  }

  update(id: number, pool: Pool): Observable<any> {
    return this.http.put('/api/pools/' + id, pool);
  }

  getFishes(id: number): Observable<any> {
    return this.http.get('/api/pools/' + id + '/fishes');
  }

  getSchedules(id: number): Observable<any> {
    return this.http.get('/api/pools/' + id + '/schedules');
  }
}
