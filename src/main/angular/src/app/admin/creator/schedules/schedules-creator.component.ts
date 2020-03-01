import {Component, EventEmitter, OnInit} from '@angular/core';
import {ScheduleService} from '../../../service/schedule.service';
import {Schedule} from '../../../model/Schedule';
import {Pool} from '../../../model/Pool';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PoolService} from '../../../service/pool.service';


@Component({
  selector: 'app-schedules-creator',
  templateUrl: './schedules-creator.component.html',
  styleUrls: ['./schedules-creator.component.css']
})
export class SchedulesCreatorComponent implements OnInit {
  pools: Array<Pool> = [];
  schedules: Array<Schedule> = [];

  schedule: Schedule;

  form = new FormGroup({
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    pool: new FormControl(null, Validators.required)
  });

  onError = new EventEmitter<string>();

  constructor(private scheduleService: ScheduleService, private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.scheduleService.getAll().subscribe(
      data => {
        if (data != null) {
          this.schedules = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
    this.poolService.getAllByResponsible().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  save() {
    this.scheduleService.save(this.form.value).subscribe(schedule => {
      if (schedule != null) {
        this.form.reset();
        this.refresh();
      }
    }, error => this.onError.emit(error.error.message));
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1) +
      '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }

  isValid() {
    return !(this.form.valid && this.form.value.startPeriod <= this.form.value.endPeriod);
  }
}
