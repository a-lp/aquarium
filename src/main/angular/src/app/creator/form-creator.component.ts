import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {SpeciesService} from '../service/species.service';
import {Specie} from '../model/Specie';

@Component({
  selector: 'app-form-creator',
  templateUrl: './form-creator.component.html',
  styleUrls: ['./form-creator.component.css']
})
export class FormCreatorComponent implements OnInit {

  @Output()
  refreshEvent = new EventEmitter();
  species: Array<Specie>;

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: any) {
    this.speciesService.getAll().subscribe(data => {
      this.species = data;
      this.refreshEvent.emit(null);
    });
  }
}
