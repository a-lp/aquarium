import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Staff} from '../../../../model/Staff';
import {StaffService} from '../../../../service/staff.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {StaffRole} from '../../../../model/StaffRole';
import {Pool} from '../../../../model/Pool';
import {Sector} from '../../../../model/Sector';
import {PoolActivity} from '../../../../model/PoolActivity';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-staff-creator',
  templateUrl: './staff-creator.component.html',
  styleUrls: ['./staff-creator.component.css']
})
export class StaffCreatorComponent implements OnInit {
  @Output()
  onChange: EventEmitter<Staff> = new EventEmitter<Staff>();
  @Output()
  onError: EventEmitter<string> = new EventEmitter<string>();
  @Output()
  onHide: EventEmitter<Staff> = new EventEmitter<Staff>();
  @Input()
  staff: Staff = null;

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    birthday: new FormControl('', Validators.required),
    socialSecurity: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
  });
  roles: Array<StaffRole> = Object.values(StaffRole);
  pools: Array<Pool> = [];
  sectors: Array<Sector> = [];
  activities: Array<PoolActivity> = [];

  constructor(private staffService: StaffService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.getPools();
    this.getSectors();
    this.getActivities();
  }

  hideStaff() {
    this.onHide.emit();
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1)
      + '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }

  update() {
    this.form.value.birthday = new Date(this.form.value.birthday).getTime();
    this.staffService.update(this.staff.id, this.form.value).subscribe(
      updateStaff => {
        this.onChange.emit(null);
      }, error => this.onError.emit(error)
    );
  }

  getPools() {
    this.staffService.getPools(this.staff.id).subscribe(
      pools => this.pools = pools,
      error => this.onError.emit(error)
    );
  }

  getSectors() {
    this.staffService.getSectors(this.staff.id).subscribe(
      sectors => this.sectors = sectors,
      error => this.onError.emit(error)
    );
  }

  getActivities() {
    this.staffService.getActivities(this.staff.id).subscribe(
      activities => this.activities = activities,
      error => this.onError.emit(error)
    );
  }
}
