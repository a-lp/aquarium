import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Staff} from "../../../../model/Staff";
import {StaffService} from "../../../../service/staff.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-staff-list',
  templateUrl: './staff-list.component.html',
  styleUrls: ['./staff-list.component.css']
})
export class StaffListComponent implements OnInit {
  @Output()
  onChange: EventEmitter<Staff> = new EventEmitter<Staff>();
  @Output()
  onSelect: EventEmitter<Staff> = new EventEmitter<Staff>();
  @Input()
  staffs: Array<Staff> = [];

  constructor(private staffService: StaffService, private datePipe: DatePipe) {
  }

  ngOnInit() {
  }

  removeStaff(staff: Staff) {
    this.staffService.delete(staff).subscribe(
      removedStaff => {
        this.onChange.emit(removedStaff);
      }, error => {
        console.log(error);
      }
    );
  }

  selectStaff(staff: Staff) {
    this.onSelect.emit(staff);
  }
}
