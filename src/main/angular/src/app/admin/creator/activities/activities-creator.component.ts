import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../model/PoolActivity';
import {Schedule} from '../../../model/Schedule';
import {Staff} from '../../../model/Staff';
import {ActivityService} from '../../../service/activity.service';

@Component({
  selector: 'app-activities-creator',
  templateUrl: './activities-creator.component.html',
  styleUrls: ['./activities-creator.component.css']
})
export class ActivitiesCreatorComponent implements OnInit {
  @Input()
  activities: Array<PoolActivity>;
  @Output()
  onChange = new EventEmitter<PoolActivity>();
  @Input()
  schedules: Array<Schedule>;
  @Input()
  staffs: Array<Staff>;

  constructor(private activityService: ActivityService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  onSaveActivity(activity: PoolActivity) {
    this.onChange.emit(activity);
    this.refresh(activity);
  }

  private refresh(activity: PoolActivity) {
    this.activityService.getAll().subscribe(activities => {
      this.activities = activities;
    });
  }

  removeActivity(activity: PoolActivity) {
    this.activityService.delete(activity).subscribe(removedActivity => {
      this.refresh(removedActivity);
      this.onChange.emit(removedActivity);
    }, error => console.log(error));
  }
}
