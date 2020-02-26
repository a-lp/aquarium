import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Sector} from '../model/Sector';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  constructor(private http: HttpClient) {
  }

  save(sector: Sector): Observable<any> {
    const resp = sector.staffList;
    sector.staffList = [];
    return this.http.post('/api/sectors/responsible/' + resp, sector);
  }

  getAll(): Observable<any> {
    return this.http.get('/api/sectors');
  }

  delete(sector: Sector): Observable<any> {
    return this.http.delete('/api/sectors/' + sector.name);
  }

  getSector(name: string): Observable<any> {
    return this.http.get('/api/sectors/' + name);
  }

  update(id: number, sector: Sector): Observable<any> {
    return this.http.put('/api/sectors/' + id, sector);
  }

  getStaffList(name: string): Observable<any> {
    return this.http.get('/api/sectors/' + name + '/staffs');
  }

  getPools(name: string): Observable<any> {
    return this.http.get('/api/sectors/' + name + '/pools');
  }
}
