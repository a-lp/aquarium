import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Specie} from '../../../../model/Specie';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../../../../model/Alimentation';
import {SpeciesService} from "../../../../service/species.service";
import {DatePipe} from "@angular/common";
import {FishService} from "../../../../service/fish.service";
import {Fish} from "../../../../model/Fish";

@Component({
  selector: 'app-specie-creator',
  templateUrl: './specie-creator.component.html',
  styleUrls: ['./specie-creator.component.css']
})
export class SpecieCreatorComponent implements OnInit {
  @Input()
  specie: Specie;
  @Output()
  onHide: EventEmitter<any> = new EventEmitter<any>();
  @Output()
  onUpdate: EventEmitter<any> = new EventEmitter<any>();

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl('', Validators.required),
    extinctionLevel: new FormControl('', Validators.required),
    alimentation: new FormControl('', Validators.required)
  });

  alimentations = Object.values(Alimentation);
  field = 0; // Sorting field
  ascendent = true;
  fishList: Array<Fish> = []

  constructor(private speciesService: SpeciesService, private fishService: FishService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.refresh();
  }


  hideSpecie() {
    this.onHide.emit();
  }

  update() {
    var specie: any = Object.assign({}, this.specie);
    specie.name = this.form.value.name;
    specie.lifeSpan = this.form.value.lifeSpan;
    specie.extinctionLevel = this.form.value.extinctionLevel;
    specie.alimentation = this.form.value.alimentation;
    //TODO: implementare rimozione dei pesci da specie
    specie.fishList = this.specie.fishList.map(x => x.toString()).reduce((x, y) => x + ',' + y);
    this.speciesService.update(this.specie.name, specie).subscribe(updatedSpecie => {
      this.specie = updatedSpecie;
      this.refresh();
      this.onUpdate.emit(updatedSpecie);
    }, error => console.log(error));
  }

  refresh() {
    this.getFishes();
    this.speciesService.getSpecie(this.specie.name).subscribe(
      specie => {
        this.specie = specie;
      }, error => console.log(error)
    );
  }

  retireAnimal(fish: Fish) {
    this.fishService.retireFish(fish).subscribe(
      data => {
        this.refresh();
        this.onUpdate.emit(fish);
      },
      error => console.log(error)
    );
  }

  sort(field: number) {
    if (this.field == field) {
      this.ascendent = !this.ascendent;
    } else {
      this.ascendent = true;
    }
    this.field = field;
    this.fishService.sort(this.specie.fishList, this.field, this.ascendent);
  }

  getFishes() {
    this.speciesService.getFishes(this.specie.name).subscribe(result => {
        this.fishList = result;
      }, error => console.log(error)
    );
  }
}
