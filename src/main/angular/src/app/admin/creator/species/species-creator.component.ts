import {Component, EventEmitter, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {SpeciesService} from '../../../service/species.service';
import {Specie} from '../../../model/Specie';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../../../model/Alimentation';

@Component({
  selector: 'app-species-creator',
  templateUrl: './species-creator.component.html',
  styleUrls: ['./species-creator.component.css']
})
export class SpeciesCreatorComponent implements OnInit, OnChanges {
  species: Array<Specie> = [];
  specie: Specie;

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl(''),
    extinctionLevel: new FormControl(''),
    alimentation: new FormControl('')
  });
  alimentations = Object.values(Alimentation);
  onError = new EventEmitter<string>();

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.speciesService.getAll().subscribe(
      data => {
        if (data != null) {
          this.species = data;
        }
      }, error => this.onError.emit(error)
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
        this.refresh();
      }, error => {
        this.onError.emit(error);
      }
    );
  }

  save() {
    this.speciesService.save(this.form.value).subscribe(specie => {
      if (specie != null) {
        this.form.reset();
        this.refresh();
      }
    }, error => this.onError.emit(error));
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.refresh();
  }
}
