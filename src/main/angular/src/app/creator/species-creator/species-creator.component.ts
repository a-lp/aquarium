import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Animal} from "../../model/Animal";
import {Alimentation} from "../../model/Alimentation";
import {SpeciesService} from "../../service/species.service";

@Component({
  selector: 'app-species-creator',
  templateUrl: './species-creator.component.html',
  styleUrls: ['./species-creator.component.css']
})
export class SpeciesCreatorComponent implements OnInit {
  profileForm = new FormGroup({
    name: new FormControl("", Validators.required),
    lifeSpan: new FormControl(""),
    extinctionLevel: new FormControl(""),
    alimentation: new FormControl("")
  });
  @Output()
  onSaveSpecie: EventEmitter<Animal> = new EventEmitter<Animal>()

  alimentations = Object.values(Alimentation);

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.speciesService.save(this.profileForm.value)
    this.onSaveSpecie.emit()
  }
}
