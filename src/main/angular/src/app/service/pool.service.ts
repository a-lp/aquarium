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
    return this.http.post('/pools', pool);
  }

  getAll(): Observable<any> {
    return this.http.get('/pools');
  }

  getPool(id: string): Observable<any> {
    return this.http.get('/pools/' + id);
  }
}
