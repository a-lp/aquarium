import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Animal} from "../../model/Animal";
import {AnimalService} from "../../service/animal.service";

@Component({
  selector: 'app-animal',
  templateUrl: './animal.component.html',
  styleUrls: ['./animal.component.css']
})
export class AnimalComponent implements OnInit {
  @Input()
  animal: Animal;
  @Output()
  retireEvent: EventEmitter<Animal> = new EventEmitter<Animal>();

  constructor(private animalService: AnimalService) {

  }

  ngOnInit() {
  }

  retireAnimal() {
    this.animalService.retireAnimal(this.animal).subscribe(
      data => {
        this.retireEvent.emit()
      },
      error => console.log(error)
    );
  }
}
