import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {Staff} from "../../../../model/Staff";
import {Pool} from "../../../../model/Pool";
import {Sector} from "../../../../model/Sector";
import {SectorService} from "../../../../service/sector.service";

@Component({
  selector: 'app-sector-creator',
  templateUrl: './sector-creator.component.html',
  styleUrls: ['./sector-creator.component.css']
})
export class SectorCreatorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
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
    this.form.addControl('staffList', new FormArray(this.selectedStaff.map(x => new FormControl(x.id))));
    this.sectorService.save(this.form.value).subscribe(sector => {
        this.onSave.emit(sector);
        if (sector != null) {
          this.form.reset();
        }
      }
    );
  }

  isDisabled() {
    return !(this.form.valid && this.selectedStaff.length > 0);
  }

  selectElement(obj: any) {
    if (this.selectedStaff.includes(obj)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => obj.id == element.id));
    } else {
      this.selectedStaff.push(obj);
    }
  }
}
