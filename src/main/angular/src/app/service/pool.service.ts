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
    console.log(pool)
    const sectorId = pool.sector;
    pool.sector = null;
    return this.http.post('/sectors/' + sectorId + '/pools', pool);
  }

  getAll(): Observable<any> {
    return this.http.get('/pools');
  }

  getPool(id: string): Observable<any> {
    return this.http.get('/pools/' + id);
  }
}
