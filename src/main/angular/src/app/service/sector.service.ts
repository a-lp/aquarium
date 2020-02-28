import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Sector} from '../model/Sector';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  save(sector: Sector): Observable<any> {
    const resp = sector.staffList;
    sector.staffList = [];
    return this.authenticationService.postRequest('/api/sectors/responsible/' + resp, sector);
  }

  getAll(): Observable<any> {
    return this.authenticationService.getRequest('/api/sectors');
  }

  delete(sector: Sector): Observable<any> {
    return this.authenticationService.deleteRequest('/api/sectors/' + sector.name);
  }

  getSector(name: string): Observable<any> {
    return this.authenticationService.getRequest('/api/sectors/' + name);
  }

  update(id: number, sector: Sector): Observable<any> {
    return this.authenticationService.putRequest('/api/sectors/' + id, sector);
  }

  getStaffList(name: string): Observable<any> {
    return this.authenticationService.getRequest('/api/sectors/' + name + '/staffs');
  }

  getPools(name: string): Observable<any> {
    return this.authenticationService.getRequest('/api/sectors/' + name + '/pools');
  }

  getAllByResponsible(id: number) {
    return this.authenticationService.getRequest('/api/sectors/' + name + '/pools');
  }
}
