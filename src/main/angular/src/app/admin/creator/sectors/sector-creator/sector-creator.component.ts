import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SectorService} from '../../../../service/sector.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Staff} from '../../../../model/Staff';
import {Sector} from '../../../../model/Sector';
import {Pool} from "../../../../model/Pool";

@Component({
  selector: 'app-sector-creator',
  templateUrl: './sector-creator.component.html',
  styleUrls: ['./sector-creator.component.css']
})
export class SectorCreatorComponent implements OnInit {
  @Output()
  onHideSector = new EventEmitter();
  @Output()
  onChange = new EventEmitter();
  @Input()
  sector: Sector;
  @Input()
  staffs: Array<Staff>;

  selectedStaff: Array<Staff> = [];

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
    staffList: new FormControl('')
  });
  pools: Array<Pool>;

  constructor(private sectorService: SectorService) {
  }

  ngOnInit() {
    this.getStaffList();
    this.getPoolList();
  }


  hideSector() {
    this.onHideSector.emit();
  }

  selectElement(obj: any) {
    if (this.isIncluded(obj)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => obj.id == element.id), 1);
    } else {
      this.selectedStaff.push(obj);
    }
  }

  update() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.sectorService.update(this.sector.id, this.form.value).subscribe(
      updateSector => this.onChange.emit()
      , error => console.log(error)
    );
  }

  getStaffList() {
    this.sectorService.getStaffList(this.sector.name).subscribe(
      staffList => {
        this.selectedStaff = staffList;
      },
      error => console.log(error)
    );
  }

  isIncluded(staff: Staff): boolean {
    for (const s of this.selectedStaff) {
      if (s.id == staff.id) {
        return true;
      }
    }
    return false;
  }

  private getPoolList() {
    this.sectorService.getPools(this.sector.name).subscribe(
      pools => this.pools = pools,
      error => console.log(error)
    );
  }
}
