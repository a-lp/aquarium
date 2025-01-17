import {Component, EventEmitter, OnInit} from '@angular/core';
import {PoolActivity} from '../../../model/PoolActivity';
import {Schedule} from '../../../model/Schedule';
import {Staff} from '../../../model/Staff';
import {ActivityService} from '../../../service/activity.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ScheduleService} from '../../../service/schedule.service';
import {StaffService} from '../../../service/staff.service';
import {AuthenticationService} from "../../../service/authentication.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-activities-creator',
  templateUrl: './activities-creator.component.html',
  styleUrls: ['./activities-creator.component.css']
})
export class ActivitiesCreatorComponent implements OnInit {
  activities: Array<PoolActivity> = [];
  schedules: Array<Schedule> = [];
  staffs: Array<Staff> = [];
  startPeriod: string;
  endPeriod: string;
  activity: PoolActivity = null;
  selectedStaff: Array<Staff> = [];
  onError = new EventEmitter<any>();
  form = new FormGroup({
    description: new FormControl('', Validators.required),
    startActivity: new FormControl('', Validators.required),
    endActivity: new FormControl('', Validators.required),
    day: new FormControl(null),
    openToPublic: new FormControl(false),
    repeated: new FormControl(false),
    schedule: new FormControl(null, Validators.required),
    staffList: new FormControl('')
  });

  constructor(private activityService: ActivityService, private scheduleService: ScheduleService,
              private staffService: StaffService, private authenticationService: AuthenticationService,
              private datePipe: DatePipe) {
    this.form.get('schedule').valueChanges.subscribe(changes => {
        if (changes != null) {
          this.scheduleService.getById(changes).subscribe(
            schedule => {
              this.startPeriod = this.convertDate(schedule.startPeriod);
              this.endPeriod = this.convertDate(schedule.endPeriod);
            }
          )
          this.staffService.getBySchedulesFromPoolSector(changes).subscribe(data => {
            if (data != null) {
              this.staffs = data;
              this.selectedStaff = [];
            }
          }, error => this.onError.emit(error.error.message));
        }
      }
    );
  }

  ngOnInit() {
    this.refresh();
  }

  private refresh() {
    this.activityService.getAll().subscribe(data => {
      if (data != null) {
        this.activities = data;
      }
    }, error => this.onError.emit(error.error.message));
    this.scheduleService.getAll().subscribe(data => {
      if (data != null) {
        this.schedules = data;
      }
    }, error => this.onError.emit(error.error.message));

  }

  removeActivity(activity: PoolActivity) {
    this.activityService.delete(activity).subscribe(removedActivity => {
      this.refresh();
    }, error => this.onError.emit(error.error.message));
  }

  save() {
    this.form.value.staffList = this.selectedStaff.map(x => x.id.toString()).reduce((x, y) => x + ',' + y);
    this.activityService.save(this.form.value).subscribe(activity => {
        if (activity != null) {
          this.form.reset();
          this.refresh();
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  isDisabled() {
    const wrongDayPeriod = (this.form.value.day != null ? (this.form.value.day < this.startPeriod || this.form.value.day > this.endPeriod) : (this.form.value.repeated == false));
    const wrongHours = this.form.value.startActivity >= this.form.value.endActivity;
    const noStaffChoice = this.selectedStaff.length == 0;
    return !this.form.valid || wrongHours || wrongDayPeriod || noStaffChoice;
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

  private convertDate(date: string) {
    return date.substr(0, 10);
  }

}
