import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Specie} from "../../../../model/Specie";
import {Schedule} from "../../../../model/Schedule";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ScheduleService} from "../../../../service/schedule.service";

@Component({
  selector: 'app-schedule-creator',
  templateUrl: './schedule-creator.component.html',
  styleUrls: ['./schedule-creator.component.css']
})
export class ScheduleCreatorComponent implements OnInit {
  @Input()
  pools: Array<Specie>;
  @Output()
  onSave: EventEmitter<Schedule> = new EventEmitter<Schedule>();

  form = new FormGroup({
    startPeriod: new FormControl('', Validators.required),
    endPeriod: new FormControl('', Validators.required),
    pool: new FormControl(null, Validators.required)
  });

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit() {
  }

  save() {
    this.scheduleService.save(this.form.value).subscribe(schedule => {
      this.onSave.emit(schedule);
      if (schedule != null) {
        this.form.reset();
      }
    });
  }

  today() {
    const today = new Date();
    return (today.getFullYear() + '-' + ((today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : today.getMonth() + 1) +
      '-' + (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()));
  }
}
