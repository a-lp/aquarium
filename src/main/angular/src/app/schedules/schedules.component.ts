import {Component, Input, OnInit} from '@angular/core';
import {Schedule} from "../model/Schedule";
import {Specie} from "../model/Specie";
import {ScheduleService} from "../service/schedule.service";

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
    this.refresh(null);
  }

  refresh($event: Specie) {
    this.scheduleService.getAll().subscribe(
      data => {
        this.schedules = data;
      },
      error => console.log(error)
    );
  }
}
