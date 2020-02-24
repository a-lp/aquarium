import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ScheduleService} from '../../../service/schedule.service';
import {Schedule} from '../../../model/Schedule';
import {Pool} from '../../../model/Pool';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-schedules-creator',
  templateUrl: './schedules-creator.component.html',
  styleUrls: ['./schedules-creator.component.css']
})
export class SchedulesCreatorComponent implements OnInit {
  @Input()
  pools: Array<Pool>
  @Output()
  onChange: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Input()
  schedules: Array<Schedule> = [];

  schedule: Schedule;

  form = new FormGroup({
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    pool: new FormControl(null, Validators.required)
  });

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.scheduleService.getAll().subscribe(
      data => {
        this.schedules = data;
      },
      error => console.log(error)
    );
  }

  save() {
    this.scheduleService.save(this.form.value).subscribe(schedule => {
      this.onChange.emit(schedule);
      if (schedule != null) {
        this.form.reset();
      }
    });
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1) +
      '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }
}
