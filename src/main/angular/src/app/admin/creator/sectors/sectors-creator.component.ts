import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SectorService} from '../../../service/sector.service';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {Staff} from '../../../model/Staff';
import {PoolService} from '../../../service/pool.service';

@Component({
  selector: 'app-sectors-creator',
  templateUrl: './sectors-creator.component.html',
  styleUrls: ['./sectors-creator.component.css']
})
export class SectorsCreatorComponent implements OnInit {
  @Input()
  pools: Array<Pool> = [];
  @Input()
  staffs: Array<Staff> = [];
  @Output()
  onChange: EventEmitter<Sector> = new EventEmitter<Sector>();

  @Input()
  sectors: Array<Sector>;

  constructor(private sectorService: SectorService, private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh(sector: Sector) {
    this.sectorService.getAll().subscribe(
      data => {
        if (data != null) {
          this.sectors = data;
        }
      },
      error => console.log(error)
    );
  }

  onSaveSector(sector: Sector) {
    this.refresh(sector);
    this.onChange.emit(sector);
  }

  removeSector(sector: Sector) {
    this.sectorService.delete(sector).subscribe(
      removedSector => {
        this.refresh(removedSector);
        this.onChange.emit(removedSector);
      }, error => {
        console.log(error);
      }
    );
  }
}
