import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Specie} from "../../../model/Specie";
import {PoolService} from "../../../service/pool.service";
import {ScheduleService} from "../../../service/schedule.service";
import {Schedule} from "../../../model/Schedule";

@Component({
  selector: 'app-schedule-creator',
  templateUrl: './schedule-creator.component.html',
  styleUrls: ['./schedule-creator.component.css']
})
export class ScheduleCreatorComponent implements OnInit {
  @Input()
  pools: Array<Specie>;
  @Output()
  onSave: EventEmitter<Schedule> = new EventEmitter<Schedule>();

  form = new FormGroup({
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    pool: new FormControl(null, Validators.required)
  });

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit() {
  }

  save($event: MouseEvent) {
    this.scheduleService.save(this.form.value).subscribe(schedule => {
      this.onSave.emit(schedule);
      if (schedule != null) this.form.reset();
    });
  }
}
