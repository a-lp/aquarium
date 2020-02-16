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
    return this.http.post('/sectors/responsible/' + resp, sector);
  }

  getAll(): Observable<any> {
    return this.http.get('/sectors');
  }

  delete(sector: Sector): Observable<any> {
    return this.http.delete('/sectors/' + sector.name);
  }

  getSector(name: string): Observable<any> {
    return this.http.get('/sectors/' + name);
  }

  update(sector: Sector): Observable<any> {
    return this.http.put('/sectors/${sector.name}', sector);
  }
}
