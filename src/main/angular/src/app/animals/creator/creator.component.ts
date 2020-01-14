import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Animal} from "../../model/Animal";
import {AnimalService} from "../../service/animal.service";
import {AnimalGender} from "../../model/AnimalGender";

@Component({
  selector: 'app-creator',
  templateUrl: './creator.component.html',
  styleUrls: ['./creator.component.css']
})
export class CreatorComponent implements OnInit {
  profileForm = new FormGroup({
    name: new FormControl("", Validators.required),
    distinctSign: new FormControl("", Validators.required),
    gender: new FormControl("", Validators.required)
  });
  @Output()
  onSave: EventEmitter<Animal> = new EventEmitter<Animal>()
  genders = Object.values(AnimalGender);

  constructor(private animalService: AnimalService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.animalService.save(this.profileForm.value).subscribe(
      data => {
        this.onSave.emit()
      },
      error => console.log(error)
    )
  }
}
