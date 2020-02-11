import {Component, Input, OnInit} from '@angular/core';
import {Staff} from "../model/Staff";
import {StaffService} from "../service/staff.service";

@Component({
  selector: 'app-staffs',
  templateUrl: './staffs.component.html',
  styleUrls: ['./staffs.component.css']
})
export class StaffsComponent implements OnInit {
  @Input()
  staffs: Array<Staff> = [];

  constructor(private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Staff) {
    this.staffService.getAll().subscribe(staffs => {
      if (staffs != null) this.staffs = staffs;
    })
  }
}
