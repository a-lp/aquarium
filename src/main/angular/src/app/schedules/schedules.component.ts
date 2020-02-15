import {Component, Input, OnInit} from '@angular/core';
import {Schedule} from '../model/Schedule';
import {ScheduleService} from '../service/schedule.service';

@Component({
  selector: 'app-schedules',
  templateUrl: './schedules.component.html',
  styleUrls: ['./schedules.component.css']
})
export class SchedulesComponent implements OnInit {
  @Input()
  schedules: Array<Schedule>;

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit() {

  }


}
