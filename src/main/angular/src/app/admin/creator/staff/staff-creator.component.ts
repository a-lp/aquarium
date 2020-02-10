import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {StaffRole} from "../../../model/StaffRole";
import {StaffService} from "../../../service/staff.service";
import {Staff} from "../../../model/Staff";

@Component({
  selector: 'app-staff-creator',
  templateUrl: './staff-creator.component.html',
  styleUrls: ['./staff-creator.component.css']
})
export class StaffCreatorComponent implements OnInit {
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
      console.log(staff);
      if (staff != null) this.onSave.emit(staff);
    });
  }
}
