import {Component, Input, OnInit} from '@angular/core';
import {Schedule} from "../../model/Schedule";

@Component({
  selector: 'tr [app-schedule]',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {
  @Input()
  schedule: Schedule;

  constructor() {
  }

  ngOnInit() {
  }

}
