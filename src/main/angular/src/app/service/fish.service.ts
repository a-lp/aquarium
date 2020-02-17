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

  retireFish(fish: Fish): Observable<any> {
    return this.http.put('/fishes/retire/' + fish.id, fish);
  }

  sort(fishes: Array<Fish>, field: number, ascendent: boolean) {
    switch (field) {
      case 0:
        fishes = fishes.sort((a, b) => (ascendent ? 1 : -1) * a.id - b.id);
        break;
      case 1:
        fishes = fishes.sort((a, b) => (ascendent ? 1 : -1) * a.name.localeCompare(b.name));
        break;
      case 2:
        fishes = fishes.sort((a, b) => (ascendent ? 1 : -1) * a.gender.localeCompare(b.gender));
        break;
      case 3:
        fishes = fishes.sort((a, b) => (ascendent ? 1 : -1) * (a.arrivalDate == b.arrivalDate ? 0 : (a.arrivalDate > b.arrivalDate ? 1 : -1)));
        break;
      case 4:
        fishes = fishes.sort((a, b) => (ascendent ? 1 : -1) * (a.returnDate == b.returnDate ? 0 : (a.returnDate > b.returnDate ? 1 : -1)));
        break;
      case 5:
        fishes = fishes.sort((a, b) => {
          if (a.specie == null) {
            return (ascendent ? 1 : -1);
          }
          if (b.specie == null) {
            return (ascendent ? -1 : 1);
          }
          return ((ascendent ? 1 : -1) * a.specie.name.localeCompare(b.specie.name));
        });
        break;
      case 6:
        fishes = fishes.sort((a, b) => {
          if (a.pool == null) {
            return (ascendent ? 1 : -1);
          }
          if (b.pool == null) {
            return (ascendent ? -1 : 1);
          }
          return (ascendent ? 1 : -1) * a.pool.id - b.pool.id;
        });
        break;
    }
  }
}
