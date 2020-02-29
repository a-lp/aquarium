import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../../model/PoolActivity';
import {Schedule} from '../../../../model/Schedule';
import {Staff} from '../../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivityService} from '../../../../service/activity.service';
import {ScheduleService} from "../../../../service/schedule.service";
import {StaffService} from "../../../../service/staff.service";

@Component({
  selector: 'app-activity-creator',
  templateUrl: './activity-creator.component.html',
  styleUrls: ['./activity-creator.component.css']
})
export class ActivityCreatorComponent implements OnInit {
  @Output()
  onUpdate = new EventEmitter<PoolActivity>();
  @Output()
  onError = new EventEmitter<any>();
  @Output()
  onHide = new EventEmitter();
  @Input()
  schedules: Array<Schedule>;
  staffs: Array<Staff> = [];
  @Input()
  activity: PoolActivity;
  @Input()
  selectedStaff: Array<any>;
  startPeriod: string;
  endPeriod: string;
  form = new FormGroup({
    description: new FormControl('', Validators.required),
    startActivity: new FormControl('', Validators.required),
    endActivity: new FormControl('', Validators.required),
    openToPublic: new FormControl(false),
    day: new FormControl('', Validators.required),
    repeated: new FormControl(false),
    schedule: new FormControl(null, Validators.required),
    staffList: new FormControl('')
  });

  constructor(private activityService: ActivityService, private scheduleService: ScheduleService,
              private staffService: StaffService) {
    this.form.get('schedule').valueChanges.subscribe(changes => {
        if (this.form.value.schedule != null) {
          this.scheduleService.getById(changes).subscribe(
            schedule => {
              this.startPeriod = this.convertDate(schedule.startPeriod);
              this.endPeriod = this.convertDate(schedule.endPeriod);
            }
          );
          this.staffService.getBySchedulesFromPoolSector(changes).subscribe(data => {
            if (data != null) {
              this.staffs = data;
            }
          }, error => this.onError.emit(error.error.message));
        }
      }
    );
  }

  ngOnInit() {
    this.staffService.getBySchedulesFromPoolSector(this.activity.schedule).subscribe(data => {
      if (data != null) {
        this.staffs = data;
      }
    }, error => this.onError.emit(error.error.message));
  }


  hideActivity() {
    this.onHide.emit();
  }

  update() {
    this.form.value.staffList = this.selectedStaff.map(x => x.toString()).reduce((x, y) => x + ',' + y);
    this.activityService.update(this.activity.id, this.form.value).subscribe(activity => {
        this.onUpdate.emit(activity);
      }, error => this.onError.emit(error)
    );
  }


  selectStaff(staff: Staff) {
    if (this.isIncluded(staff)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => staff.id == element), 1);
    } else {
      this.selectedStaff.push(staff);
    }
  }

  isDisabled() {
    return !(this.form.valid && this.selectedStaff.length > 0) || (this.form.value.startActivity >= this.form.value.endActivity);
  }

  isIncluded(i: Staff) {
    for (const s of this.selectedStaff) {
      if (s == i.id) {
        return true;
      }
    }
    return false;
  }

  private convertDate(date: any) {
    return date.substr(0, 10);
  }

}
