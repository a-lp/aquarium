import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Specie} from '../model/Specie';

@Injectable({
  providedIn: 'root'
})
export class SpeciesService {

  save(specie: Specie): Observable<any> {
    return this.http.post('/api/species', specie);
  }

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/api/species');
  }

  delete(specie: Specie): Observable<any> {
    return this.http.delete('/api/species/' + specie.name);
  }

  getSpecie(name: string): Observable<any> {
    return this.http.get('/api/species/' + name);
  }

  update(name: string, specie: Specie): Observable<any> {
    return this.http.put('/api/species/' + name, specie);
  }

  getFishes(name: string): Observable<any> {
    return this.http.get('/api/species/' + name + '/fishes');
  }
}
