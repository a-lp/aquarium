import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Staff} from '../../../../model/Staff';
import {Pool} from '../../../../model/Pool';
import {Sector} from '../../../../model/Sector';
import {SectorService} from '../../../../service/sector.service';

@Component({
  selector: 'app-sector-creator',
  templateUrl: './sector-creator.component.html',
  styleUrls: ['./sector-creator.component.css']
})
export class SectorCreatorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
    staffList: new FormControl('')
  });
  selectedStaff: Array<Staff> = [];
  @Input()
  pools: Array<Pool> = [];
  @Input()
  staffs: Array<Staff> = [];

  @Output()
  onSave: EventEmitter<Sector> = new EventEmitter<Sector>();

  constructor(private sectorService: SectorService) {
  }

  ngOnInit() {
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.sectorService.save(this.form.value).subscribe(sector => {
        this.onSave.emit(sector);
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
}
