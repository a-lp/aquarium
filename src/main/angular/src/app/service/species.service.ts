import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Specie} from '../model/Specie';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SpeciesService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  save(specie: Specie): Observable<any> {
    return this.authenticationService.postRequest('/api/species', specie);
  }

  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/species');
  }

  delete(specie: Specie): Observable<any> {
    return this.authenticationService.deleteRequest('/api/species/' + specie.name);
  }

  getSpecie(name: string): Observable<any> {
    return this.authenticationService.getRequest('/api/species/' + name);
  }

  update(name: string, specie: Specie): Observable<any> {
    return this.authenticationService.putRequest('/api/species/' + name, specie);
  }

  getFishes(name: string): Observable<any> {
    return this.authenticationService.getRequest('/api/species/' + name + '/fishes');
  }
}
