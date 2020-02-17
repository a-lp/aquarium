import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Specie} from '../../../../model/Specie';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../../../../model/Alimentation';
import {SpeciesService} from "../../../../service/species.service";

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

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
  }


  hideSpecie() {
    this.onHide.emit();
  }

  update() {
    //TODO: gestire l'update dei pesci
    var specie: any = Object.assign({}, this.specie);
    specie.name = this.form.value.name;
    specie.lifeSpan = this.form.value.lifeSpan;
    specie.extinctionLevel = this.form.value.extinctionLevel;
    specie.alimentation = this.form.value.alimentation;
    //specie.fishList = this.specie.fishList.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    specie.fishList = this.specie.fishList[0].id;
    console.log(specie);
    this.speciesService.update(this.specie.name, specie).subscribe(updatedSpecie => {
      console.log(updatedSpecie);
      this.onUpdate.emit(updatedSpecie);
    }, error => console.log(error));
  }

}
