import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Sector} from '../model/Sector';
import {Observable} from 'rxjs';
import {Pool} from '../model/Pool';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  constructor(private http: HttpClient) {
  }

  save(sector: Sector): Observable<any> {
    return this.http.post('/sectors', sector);
  }

  addPoolToSector(name: string, pool: Pool): Observable<any> {
    // TODO: gestire la persistenza di pool.
    return this.http.post('/sectors/' + name + '/assign-pool', pool);
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
