import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../../model/PoolActivity';
import {Schedule} from '../../../../model/Schedule';
import {Staff} from '../../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivityService} from '../../../../service/activity.service';

@Component({
  selector: 'app-activity-creator',
  templateUrl: './activity-creator.component.html',
  styleUrls: ['./activity-creator.component.css']
})
export class ActivityCreatorComponent implements OnInit {
  @Output()
  onUpdate = new EventEmitter<PoolActivity>();
  @Output()
  onHide = new EventEmitter();
  @Input()
  schedules: Array<Schedule>;
  @Input()
  staffs: Array<Staff>;
  @Input()
  activity: PoolActivity;
  @Input()
  selectedStaff: Array<number>;

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
  }


  hideActivity() {
    this.onHide.emit();
  }

  update() {
    this.form.value.staffList = this.selectedStaff.map(x => x.toString()).reduce((x, y) => x + ',' + y);
    this.activityService.update(this.activity.id, this.form.value).subscribe(activity => {
        this.onUpdate.emit(activity);
      }, error => console.log(error)
    );
  }


  selectStaff(staff: Staff) {
    if (this.selectedStaff.includes(staff.id)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => staff.id == element), 1);
    } else {
      this.selectedStaff.push(staff.id);
    }
  }

  isDisabled() {
    return !(this.form.valid && this.selectedStaff.length > 0);
  }

  isIncluded(i: Staff) {
    for (const s of this.selectedStaff) {
      if (s == i.id) {
        return true;
      }
    }
    return false;
  }
}
