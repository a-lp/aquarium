import {Component, OnInit} from '@angular/core';
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
      },
      error => console.log(error)
    );
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      },
      error => console.log(error)
    );
    this.staffService.getAll().subscribe(
      data => {
        if (data != null) {
          this.staffs = data.filter(staff => staff.role === StaffRole.MANAGER);
        }
      },
      error => console.log(error)
    );
  }

  removeSector(sector: Sector) {
    this.sectorService.delete(sector).subscribe(
      removedSector => {
        this.refresh();
      }, error => {
        console.log(error);
      }
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
