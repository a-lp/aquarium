import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
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
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl('', Validators.required),
  });
  @Output()
  onSave: EventEmitter<Sector> = new EventEmitter<Sector>();
  selectedStaff: Array<Staff> = [];

  constructor(private sectorService: SectorService, private poolService: PoolService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.form.addControl('staffList', new FormArray(this.selectedStaff.map(x => new FormControl(x.id))));
    this.sectorService.save(this.form.value).subscribe(sector => {
        console.log(sector);
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
