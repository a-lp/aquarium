import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Animal} from "../../model/Animal";
import {AnimalService} from "../../service/animal.service";
import {AnimalGender} from "../../model/AnimalGender";
import {Specie} from "../../model/Specie";
import {SpeciesService} from "../../service/species.service";

@Component({
  selector: 'app-animals-creator',
  templateUrl: './animals-creator.component.html',
  styleUrls: ['./animals-creator.component.css']
})
export class AnimalsCreatorComponent implements OnInit {
  profileForm = new FormGroup({
    name: new FormControl("", Validators.required),
    distinctSign: new FormControl("", Validators.required),
    gender: new FormControl("", Validators.required),
    specie: new FormControl("", Validators.required)
  });

  @Output()
  onSave: EventEmitter<Animal> = new EventEmitter<Animal>()
  genders = Object.values(AnimalGender);
  @Input()
  species: Array<Specie>

  constructor(private animalService: AnimalService, private speciesService: SpeciesService) {
  }

  ngOnInit() {

  }

  save($event: Event) {
    var t: Animal = this.profileForm.value
    this.speciesService.getSpecie(this.profileForm.value["specie"]).subscribe(data => t.specie = data)
    this.animalService.save(t).subscribe(
      data => {
        this.onSave.emit()
      },
      error => console.log(error)
    )
  }
}
