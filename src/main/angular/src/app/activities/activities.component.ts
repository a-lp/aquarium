import {Component, Input, OnInit} from '@angular/core';
import {PoolActivity} from '../model/PoolActivity';

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.css']
})
export class ActivitiesComponent implements OnInit {
  @Input()
  activities: Array<PoolActivity>;

  constructor() {
  }

  ngOnInit() {
  }

}
