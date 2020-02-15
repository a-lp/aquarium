import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {SpeciesService} from '../../../service/species.service';
import {Specie} from '../../../model/Specie';

@Component({
  selector: 'app-species-creator',
  templateUrl: './species-creator.component.html',
  styleUrls: ['./species-creator.component.css']
})
export class SpeciesCreatorComponent implements OnInit {
  species: Array<Specie>;

  @Output()
  onChange: EventEmitter<Specie> = new EventEmitter<Specie>();

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
    console.log(specie);
  }

  onSaveSpecie(specie: Specie) {
    this.refresh(specie);
    this.onChange.emit(specie);
  }
}
