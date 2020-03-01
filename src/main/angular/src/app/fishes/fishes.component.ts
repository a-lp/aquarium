import {Component, OnInit} from '@angular/core';
import {Fish} from '../model/Fish';
import {FishService} from '../service/fish.service';
import {DatePipe} from '@angular/common';
import {Sector} from '../model/Sector';
import {SectorService} from '../service/sector.service';

@Component({
  selector: 'app-fishes',
  templateUrl: './fishes.component.html',
  styleUrls: ['./fishes.component.css']
})
export class FishesComponent implements OnInit {
  fishes: Array<Fish> = [];
  sectors: Array<Sector> = [];
  sectorFilter: Sector = null;

  constructor(private fishService: FishService, private datePipe: DatePipe,
              private sectorService: SectorService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
        }
      }
    );
    this.sectorService.getAll().subscribe(data => {
      if (data != null) {
        this.sectors = data;
      }
    });
  }

  filter(change) {
    const options = change.target.options;
    const pools: string = options[options.selectedIndex].value;
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
          if (options.selectedIndex != 0) {
            this.fishes = this.fishes.filter(x => pools.includes(x.id.toString()));
          }
        }
      }
    );
  }
}
