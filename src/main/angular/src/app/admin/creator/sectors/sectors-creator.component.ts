import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SectorService} from '../../../service/sector.service';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {Staff} from '../../../model/Staff';
import {PoolService} from '../../../service/pool.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";

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

  sector: Sector;
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
    staffList: new FormControl('')
  });
  selectedStaff: Array<Staff> = [];


  constructor(private sectorService: SectorService, private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.sectorService.getAll().subscribe(
      data => {
        if (data != null) {
          this.sectors = data;
        }
      },
      error => console.log(error)
    );
  }

  removeSector(sector: Sector) {
    this.sectorService.delete(sector).subscribe(
      removedSector => {
        this.refresh();
        this.onChange.emit(removedSector);
      }, error => {
        console.log(error);
      }
    );
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.sectorService.save(this.form.value).subscribe(sector => {
        this.refresh();
        this.onChange.emit(sector);
        if (sector != null) {
          this.form.reset();
          this.selectedStaff = [];
        }
      }
    );
  }

  isDisabled() {
    return !(this.form.valid && this.selectedStaff.length > 0);
  }

  selectElement(obj: any) {
    if (this.selectedStaff.includes(obj)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => obj.id == element.id), 1);
    } else {
      this.selectedStaff.push(obj);
    }
  }

  selectSector(sector: Sector) {
    this.sector = sector;
  }

}
