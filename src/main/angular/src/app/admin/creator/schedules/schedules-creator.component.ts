import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ScheduleService} from '../../../service/schedule.service';
import {Schedule} from '../../../model/Schedule';
import {Pool} from '../../../model/Pool';

@Component({
  selector: 'app-schedules-creator',
  templateUrl: './schedules-creator.component.html',
  styleUrls: ['./schedules-creator.component.css']
})
export class SchedulesCreatorComponent implements OnInit {
  @Input()
  pools: Array<Pool>;
  @Output()
  onChange: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  schedules: Array<Schedule> = [];

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh(schedule: Schedule) {
    this.scheduleService.getAll().subscribe(
      data => {
        this.schedules = data;
      },
      error => console.log(error)
    );
  }

  onSaveSchedule(schedule: Schedule) {
    this.onChange.emit(schedule);
    this.refresh(schedule);
  }

  removeSchedule(schedule: Schedule) {
    this.scheduleService.delete(schedule).subscribe(removedSchedule => {
      this.refresh(removedSchedule);
      this.onChange.emit(removedSchedule);
    }, error => console.log(error));
  }
}
