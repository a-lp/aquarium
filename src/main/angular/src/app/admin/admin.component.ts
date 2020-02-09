import {Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Specie} from '../model/Specie';
import {Pool} from '../model/Pool';
import {SpeciesService} from '../service/species.service';
import {PoolService} from '../service/pool.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  @Output()
  refreshEvent = new EventEmitter();
  species: Array<Specie>;
  pools: Array<Pool>;
  @ViewChild('components', {static: false})
  components: ElementRef;

  shown: number = 0;

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

  showComponent(position: number) {
    this.shown = position;
  }
}
