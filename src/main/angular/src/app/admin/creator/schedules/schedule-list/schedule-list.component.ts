import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Schedule} from '../../../../model/Schedule';
import {ScheduleService} from '../../../../service/schedule.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-schedule-list',
  templateUrl: './schedule-list.component.html',
  styleUrls: ['./schedule-list.component.css']
})
export class ScheduleListComponent implements OnInit {
  @Input()
  schedules: Array<Schedule>;
  @Output()
  onChange: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Output()
  onSelect: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Output()
  onError: EventEmitter<string> = new EventEmitter<string>();
  @Input()
  modificable = true;

  constructor(private scheduleService: ScheduleService, private datePipe: DatePipe) {
  }

  ngOnInit() {
  }

  removeSchedule(schedule: Schedule) {
    this.scheduleService.delete(schedule).subscribe(removedSchedule => {
      this.onChange.emit(removedSchedule);
    }, error => this.onError.emit(error));
  }

  selectSchedule(schedule: Schedule) {
    this.onSelect.emit(schedule);
  }
}
