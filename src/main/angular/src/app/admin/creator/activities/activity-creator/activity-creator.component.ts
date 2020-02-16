import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../../model/PoolActivity';
import {Schedule} from '../../../../model/Schedule';
import {Staff} from '../../../../model/Staff';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivityService} from '../../../../service/activity.service';

@Component({
  selector: 'app-activity-creator',
  templateUrl: './activity-creator.component.html',
  styleUrls: ['./activity-creator.component.css']
})
export class ActivityCreatorComponent implements OnInit {
  @Output()
  onSave = new EventEmitter<PoolActivity>();
  @Input()
  schedules: Array<Schedule>;
  @Input()
  staffs: Array<Staff>;
  selectedStaff: Array<Staff> = [];

  form = new FormGroup({
    description: new FormControl('', Validators.required),
    startActivity: new FormControl('', Validators.required),
    endActivity: new FormControl('', Validators.required),
    openToPublic: new FormControl(false),
    repeated: new FormControl(false),
    schedule: new FormControl('', Validators.required),
  });

  constructor(private activityService: ActivityService) {
  }

  ngOnInit() {
  }


  save() {
    this.form.addControl('staffList', new FormArray(this.selectedStaff.map(x => new FormControl(x.id))));
    this.activityService.save(this.form.value).subscribe(activity => {
        console.log(activity);
        this.onSave.emit(activity);
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
}
