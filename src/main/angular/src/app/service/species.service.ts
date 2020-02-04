import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Specie} from '../model/Specie';

@Injectable({
  providedIn: 'root'
})
export class SpeciesService {

  save(specie: Specie): Observable<any> {
    return this.http.post('/species', specie);
  }

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/species');
  }

  delete(specie: Specie): Observable<any> {
    return this.http.delete('/species/' + specie.name);
  }

  getSpecie(name: string): Observable<any> {
    return this.http.get('/species/' + name);
  }
}
