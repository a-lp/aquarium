import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Pool, WaterCondition} from '../../../../model/Pool';
import {Sector} from '../../../../model/Sector';
import {PoolService} from '../../../../service/pool.service';
import {Staff} from '../../../../model/Staff';
import {Fish} from "../../../../model/Fish";
import {FishService} from "../../../../service/fish.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-pool-creator',
  templateUrl: './pool-creator.component.html',
  styleUrls: ['./pool-creator.component.css']
})
export class PoolCreatorComponent implements OnInit {
  form = new FormGroup({
    maxCapacity: new FormControl('', Validators.required),
    volume: new FormControl('', Validators.required),
    condition: new FormControl('', Validators.required),
    sector: new FormControl('', Validators.required),
    responsible: new FormControl('', Validators.required)
  });
  conditions = Object.values(WaterCondition);
  @Output()
  onSave: EventEmitter<Pool> = new EventEmitter<Pool>();
  @Input()
  sectors: Array<Sector>;
  @Input()
  pool: Pool;
  @Input()
  staffs: Array<Staff>;
  @Output()
  onHide = new EventEmitter();
  field = 0; // Sorting field
  ascendent = true;
  fishList: Array<Fish> = [];

  constructor(private poolService: PoolService, private fishService: FishService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.getFishes();
  }

  hidePool() {
    this.onHide.emit();
  }

  update() {
    this.poolService.update(this.pool.id, this.form.value).subscribe(
      updatePool => this.onSave.emit()
      , error => console.log(error)
    );
  }

  getFishes() {
    this.poolService.getFishes(this.pool.id).subscribe(fishes => {
      console.log(fishes);
      this.fishList = fishes;
      console.log(fishes);
    }, error => console.log(error));
  }
}
