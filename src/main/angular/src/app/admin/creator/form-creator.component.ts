import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {SpeciesService} from '../../service/species.service';
import {Specie} from '../../model/Specie';
import {PoolService} from '../../service/pool.service';
import {Pool} from '../../model/Pool';

@Component({
  selector: 'app-form-creator',
  templateUrl: './form-creator.component.html',
  styleUrls: ['./form-creator.component.css']
})
export class FormCreatorComponent implements OnInit {

  @Output()
  refreshEvent = new EventEmitter();
  species: Array<Specie>;
  pools: Array<Pool>;

  constructor(private speciesService: SpeciesService, private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: any) {
    this.speciesService.getAll().subscribe(data => {
      this.species = data;
      this.refreshEvent.emit(null);
    });
    this.poolService.getAll().subscribe(pools => {
      this.pools = pools;
      this.refreshEvent.emit(null);
    });
  }
}
