import {Component, EventEmitter, OnInit} from '@angular/core';
import {StaffService} from '../../../service/staff.service';
import {Staff} from '../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {StaffRole} from '../../../model/StaffRole';

@Component({
  selector: 'app-staffs-creator',
  templateUrl: './staffs-creator.component.html',
  styleUrls: ['./staffs-creator.component.css']
})
export class StaffsCreatorComponent implements OnInit {
  staffs: Array<Staff> = [];
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
  onError = new EventEmitter<string>();

  constructor(private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh();
  }

  private refresh() {
    this.staffService.getAll().subscribe(staffs => {
      if (staffs != null) {
        this.staffs = staffs;
      }
    }, error => this.onError.emit(error));
  }

  save() {
    this.staffService.addStaff(this.form.value).subscribe(staff => {
      if (staff != null) {
        this.refresh();
        this.form.reset();
      }
    }, error => this.onError.emit(error));
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1)
      + '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }
}
