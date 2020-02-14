import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PoolActivity} from '../../../model/PoolActivity';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
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

  save($event: MouseEvent) {
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

  selectElement(obj: any) {
    if (this.selectedStaff.includes(obj)) {
      this.selectedStaff.splice(this.selectedStaff.findIndex(element => obj.id == element.id));
    } else {
      this.selectedStaff.push(obj);
    }
  }
}
