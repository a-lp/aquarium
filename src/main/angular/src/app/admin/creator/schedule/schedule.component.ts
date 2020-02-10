import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Specie} from "../../../model/Specie";
import {PoolService} from "../../../service/pool.service";
import {ScheduleService} from "../../../service/schedule.service";
import {Schedule} from "../../../model/Schedule";

@Component({
  selector: 'app-schedule-creator',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {
  @Input()
  pools: Array<Specie>;
  @Output()
  onSave: EventEmitter<Schedule> = new EventEmitter<Schedule>();

  form = new FormGroup({
    description: new FormControl('', Validators.required),
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    repeated: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });

  constructor(private poolService: PoolService, private scheduleService: ScheduleService) {
  }

  ngOnInit() {
  }

  save($event: MouseEvent) {
    this.poolService.getPool(this.form.value.pool).subscribe(pool => {
      if (pool != null) {
        this.form.value.pool = pool;
        this.scheduleService.save(this.form.value).subscribe(schedule => {
          console.log(schedule);
          if (schedule != null) this.onSave.emit(schedule);
        });
      }
    })
  }
}
