import {Component, EventEmitter, OnInit} from '@angular/core';
import {SectorService} from '../../../service/sector.service';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {Staff} from '../../../model/Staff';
import {PoolService} from '../../../service/pool.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {StaffService} from '../../../service/staff.service';
import {StaffRole} from '../../../model/StaffRole';

@Component({
  selector: 'app-sectors-creator',
  templateUrl: './sectors-creator.component.html',
  styleUrls: ['./sectors-creator.component.css']
})
export class SectorsCreatorComponent implements OnInit {
  pools: Array<Pool> = [];
  staffs: Array<Staff> = [];
  sectors: Array<Sector> = [];

  onError = new EventEmitter<string>();

  sector: Sector;
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
    staffList: new FormControl('')
  });
  selectedStaff: Array<Staff> = [];


  constructor(private sectorService: SectorService, private poolService: PoolService, private staffService: StaffService) {
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
      }, error => this.onError.emit(error)
    );
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }, error => this.onError.emit(error)
    );
    this.staffService.getAll().subscribe(
      data => {
        if (data != null) {
          this.staffs = data.filter(staff => staff.role === StaffRole.MANAGER);
        }
      }, error => this.onError.emit(error)
    );
  }

  removeSector(sector: Sector) {
    this.sectorService.delete(sector).subscribe(
      removedSector => {
        this.refresh();
      }, error => this.onError.emit(error)
    );
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.sectorService.save(this.form.value).subscribe(sector => {
        this.refresh();
        if (sector != null) {
          this.form.reset();
          this.selectedStaff = [];
        }
      }, error => this.onError.emit(error)
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
