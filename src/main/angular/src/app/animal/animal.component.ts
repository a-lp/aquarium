import {Component, OnInit} from '@angular/core';
import {Animal} from "./Animal";
import {AnimalService} from "./animal.service";

@Component({
  selector: 'app-animal',
  templateUrl: './animal.component.html',
  styleUrls: ['./animal.component.css']
})
export class AnimalComponent implements OnInit {
  animals: Array<Animal>

  constructor(private animalService: AnimalService) {
  }

  ngOnInit() {
    this.refresh(null)
  }

  refresh($event: Animal) {
    this.animalService.getAll().subscribe(
      data => this.animals = data,
      error => console.log(error))
  }

}
