import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SpeciesService} from '../../../service/species.service';
import {Specie} from '../../../model/Specie';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../../../model/Alimentation';

@Component({
  selector: 'app-species-creator',
  templateUrl: './species-creator.component.html',
  styleUrls: ['./species-creator.component.css']
})
export class SpeciesCreatorComponent implements OnInit {
  @Input()
  species: Array<Specie>;
  specie: Specie;
  @Output()
  onChange: EventEmitter<Specie> = new EventEmitter<Specie>();
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl(''),
    extinctionLevel: new FormControl(''),
    alimentation: new FormControl('')
  });

  alimentations = Object.values(Alimentation);

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Specie) {
    this.speciesService.getAll().subscribe(
      data => this.species = data
      ,
      error => console.log(error)
    );
  }

  showFishes(specie: Specie) {
    this.specie = specie;
  }

  hideSpecie() {
    this.specie = null;
  }

  removeSpecie(specie: Specie) {
    this.speciesService.delete(specie).subscribe(
      removedSpecie => {
        this.refresh(removedSpecie);
        this.onChange.emit(removedSpecie);
      }, error => {
        console.log(error);
      }
    );
  }

  save() {
    this.speciesService.save(this.form.value).subscribe(specie => {
      if (specie != null) {
        this.form.reset();
        this.refresh(specie);
        this.onChange.emit();
      }
    });
  }
}
