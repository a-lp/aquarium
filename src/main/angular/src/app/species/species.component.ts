import {Component, OnInit} from '@angular/core';
import {Specie} from '../model/Specie';
import {SpeciesService} from '../service/species.service';

@Component({
  selector: 'app-species',
  templateUrl: './species.component.html',
  styleUrls: ['./species.component.css']
})
export class SpeciesComponent implements OnInit {
  species: Array<Specie>;

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

}
