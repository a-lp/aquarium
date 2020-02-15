import {Component, Input, OnInit} from '@angular/core';
import {Staff} from '../../model/Staff';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit {
  @Input()
  staff: Staff;

  constructor() {
  }

  ngOnInit() {
  }

}
