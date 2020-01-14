import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Animal} from "../model/Animal";
import {Specie} from "../model/Specie";

@Injectable({
  providedIn: 'root'
})
export class SpeciesService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get("/species")
  }

  save(specie: Specie): Observable<any> {
    return this.http.post("/species", specie)
  }

  delete(specie: Specie): Observable<any> {
    return this.http.delete("/species/" + specie.name)
  }

}
