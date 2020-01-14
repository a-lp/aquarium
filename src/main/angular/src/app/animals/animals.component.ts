import {Component, OnInit} from '@angular/core';
import {Animal} from "../model/Animal";
import {AnimalService} from "../service/animal.service";

@Component({
  selector: 'app-animals',
  templateUrl: './animals.component.html',
  styleUrls: ['./animals.component.css']
})
export class AnimalsComponent implements OnInit {
  animals: Array<Animal>
  retiredAnimals: Array<Animal>

  constructor(private animalService: AnimalService) {
  }

  ngOnInit() {
    this.refresh(null)
  }

  refresh($event: Animal) {
    this.animalService.getAll().subscribe(
      data => {
        if (data != null) {
          this.retiredAnimals = data.filter(element => {
            return element.returnDate != null;
          })
          this.animals = data.filter(element => {
            return !this.retiredAnimals.includes(element);
          })
        }
      },
      error => console.log(error)
    )

  }

}
