import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {StaffRole} from '../../../model/StaffRole';
import {StaffService} from '../../../service/staff.service';
import {Staff} from '../../../model/Staff';

@Component({
  selector: 'app-staffs-creator',
  templateUrl: './staffs-creator.component.html',
  styleUrls: ['./staffs-creator.component.css']
})
export class StaffsCreatorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    birthday: new FormControl('', Validators.required),
    socialSecurity: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
  });
  roles: Array<StaffRole> = Object.values(StaffRole);
  @Output()
  onSave: EventEmitter<Staff> = new EventEmitter<Staff>();

  constructor(private staffService: StaffService) {
  }

  ngOnInit() {
  }

  save($event: MouseEvent) {
    this.staffService.addStaff(this.form.value).subscribe(staff => {
      this.onSave.emit(staff);
      if (staff != null) {
        this.form.reset();
      }
    });
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1) + '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }
}
