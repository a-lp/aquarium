import {Component, OnInit} from '@angular/core';
import {PoolActivity} from '../../../model/PoolActivity';
import {Schedule} from '../../../model/Schedule';
import {Staff} from '../../../model/Staff';
import {ActivityService} from '../../../service/activity.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ScheduleService} from "../../../service/schedule.service";
import {StaffService} from "../../../service/staff.service";

@Component({
  selector: 'app-activities-creator',
  templateUrl: './activities-creator.component.html',
  styleUrls: ['./activities-creator.component.css']
})
export class ActivitiesCreatorComponent implements OnInit {
  activities: Array<PoolActivity> = [];
  schedules: Array<Schedule> = [];
  staffs: Array<Staff> = [];

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

  constructor(private activityService: ActivityService, private scheduleService: ScheduleService, private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh();
  }

  private refresh() {
    this.activityService.getAll().subscribe(data => {
      if (data != null) {
        this.activities = data;
      }
    });
    this.scheduleService.getAll().subscribe(data => {
      if (data != null) {
        this.schedules = data;
      }
    });
    this.staffService.getAll().subscribe(data => {
      if (data != null) {
        this.staffs = data;
      }
    });
  }

  removeActivity(activity: PoolActivity) {
    this.activityService.delete(activity).subscribe(removedActivity => {
      this.refresh();
    }, error => console.log(error));
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.activityService.save(this.form.value).subscribe(activity => {
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
