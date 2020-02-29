import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Schedule} from '../../../../model/Schedule';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ScheduleService} from '../../../../service/schedule.service';
import {Pool} from '../../../../model/Pool';
import {PoolActivity} from '../../../../model/PoolActivity';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-schedule-creator',
  templateUrl: './schedule-creator.component.html',
  styleUrls: ['./schedule-creator.component.css']
})
export class ScheduleCreatorComponent implements OnInit {
  @Input()
  pools: Array<Pool>;
  @Input()
  schedule: Schedule;
  @Output()
  onHide: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Output()
  onChange: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Output()
  onError: EventEmitter<string> = new EventEmitter<string>();

  form = new FormGroup({
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    pool: new FormControl(null, Validators.required)
  });
  activities: Array<PoolActivity> = [];

  constructor(private scheduleService: ScheduleService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.getActivities();
  }

  hideSchedule() {
    this.onHide.emit(null);
  }

  update() {
    this.form.value.startPeriod = new Date(this.form.value.startPeriod).getTime();
    this.form.value.endPeriod = new Date(this.form.value.endPeriod).getTime();
    this.scheduleService.update(this.schedule.id, this.form.value).subscribe(
      updateSchedule => {
        this.onChange.emit();
      }, error => this.onError.emit(error)
    );
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1) +
      '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }

  private getActivities() {
    this.scheduleService.getActivities(this.schedule.id).subscribe(
      activities => {
        this.activities = activities;
      }, error => this.onError.emit(error)
    );
  }


  isValid() {
    return !(this.form.valid && this.form.value.startPeriod <= this.form.value.endPeriod);
  }
}
