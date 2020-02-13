import {Component, Input, OnInit} from '@angular/core';
import {PoolActivity} from '../../model/PoolActivity';

@Component({
  selector: 'tr [app-activity]',
  templateUrl: './activity.component.html',
  styleUrls: ['./activity.component.css']
})
export class ActivityComponent implements OnInit {
  @Input()
  activity: PoolActivity;

  constructor() {
  }

  ngOnInit() {
  }

}
