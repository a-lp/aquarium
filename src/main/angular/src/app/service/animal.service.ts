import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Animal} from "../model/Animal";

@Injectable({
  providedIn: 'root'
})
export class AnimalService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get("/animals")
  }

  save(animal: Animal): Observable<any> {
    return this.http.post("/animals", animal)
  }

  delete(animal: Animal): Observable<any> {
    return this.http.delete("/animals/" + animal.id)
  }

  retireAnimal(animal: Animal) {
    return this.http.put("/animals/retire/" + animal.id, animal)
  }
}