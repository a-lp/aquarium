import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Fish} from '../model/Fish';

@Injectable({
  providedIn: 'root'
})
export class FishService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('/fishes');
  }

  save(fish: Fish): Observable<any> {
    const specie = fish.specie;
    const pool = fish.pool;
    fish.specie = null;
    fish.pool = null;
    return this.http.post('/species/' + specie + '/pools/' + pool + '/fishes', fish);
  }

  delete(fish: Fish): Observable<any> {
    return this.http.delete('/fishes/' + fish.id);
  }

  retireFish(fish: Fish) {
    return this.http.put('/fishes/retire/' + fish.id, fish);
  }
}
