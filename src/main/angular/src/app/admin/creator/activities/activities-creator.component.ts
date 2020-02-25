import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../model/PoolActivity';
import {Schedule} from '../../../model/Schedule';
import {Staff} from '../../../model/Staff';
import {ActivityService} from '../../../service/activity.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";

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

  activity: PoolActivity = null;
  selectedStaff: Array<Staff> = [];
  form = new FormGroup({
    description: new FormControl('', Validators.required),
    startActivity: new FormControl('', Validators.required),
    endActivity: new FormControl('', Validators.required),
    openToPublic: new FormControl(false),
    repeated: new FormControl(false),
    schedule: new FormControl('', Validators.required),
    staffList: new FormControl('')
  });

  constructor(private activityService: ActivityService) {
  }

  ngOnInit() {
    this.refresh();
  }

  private refresh() {
    this.activityService.getAll().subscribe(activities => {
      this.activities = activities;
    });
  }

  removeActivity(activity: PoolActivity) {
    this.activityService.delete(activity).subscribe(removedActivity => {
      this.refresh();
      this.onChange.emit(removedActivity);
    }, error => console.log(error));
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.activityService.save(this.form.value).subscribe(activity => {
        this.onChange.emit(activity);
        if (activity != null) {
          this.form.reset();
        }
      }
    );
  }

  isDisabled() {
    return !(this.form.valid && this.selectedStaff.length > 0);
  }

  selectStaff(staff: Staff) {
    if (this.selectedStaff.includes(staff)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => staff.id == element.id), 1);
    } else {
      this.selectedStaff.push(staff);
    }
  }

  selectActivity(activity: PoolActivity) {
    this.activity = activity;
  }
}
