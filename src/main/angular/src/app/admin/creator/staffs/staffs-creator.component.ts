import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {StaffService} from '../../../service/staff.service';
import {Staff} from '../../../model/Staff';

@Component({
  selector: 'app-staffs-creator',
  templateUrl: './staffs-creator.component.html',
  styleUrls: ['./staffs-creator.component.css']
})
export class StaffsCreatorComponent implements OnInit {
  @Output()
  onChange: EventEmitter<Staff> = new EventEmitter<Staff>();
  staffs: Array<Staff> = [];

  constructor(private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  onSaveStaff(staff: Staff) {
    this.onChange.emit(staff);
    this.refresh(staff);
  }

  private refresh(staff: Staff) {
    this.staffService.getAll().subscribe(staffs => {
      this.staffs = staffs;
    });
  }

  removeStaff(staff: Staff) {
    this.staffService.delete(staff).subscribe(
      removedStaff => {
        this.refresh(removedStaff);
        this.onChange.emit(removedStaff);
      }, error => {
        console.log(error);
      }
    );
  }
}
