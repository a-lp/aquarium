import {Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Specie} from '../model/Specie';
import {Pool} from '../model/Pool';
import {SpeciesService} from '../service/species.service';
import {PoolService} from '../service/pool.service';
import {Fish} from "../model/Fish";
import {FishService} from "../service/fish.service";
import {ScheduleService} from "../service/schedule.service";
import {Schedule} from "../model/Schedule";
import {StaffService} from "../service/staff.service";
import {Staff} from "../model/Staff";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  @Output()
  refreshEvent = new EventEmitter();
  fishes: Array<Fish> = [];
  species: Array<Specie> = [];
  pools: Array<Pool> = [];
  staffs: Array<Staff>;
  schedules: Array<Schedule> = [];

  @ViewChild('components', {static: false})
  components: ElementRef;
  shown: number = 0;

  constructor(private fishService: FishService, private speciesService: SpeciesService,
              private poolService: PoolService, private scheduleService: ScheduleService,
              private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh(null);

  }

  refresh($event: any) {
    this.speciesService.getAll().subscribe(data => {
      this.species = data;
      this.refreshEvent.emit(null);
    });
    this.poolService.getAll().subscribe(pools => {
      this.pools = pools;
      this.refreshEvent.emit(null);
    });
    this.fishService.getAll().subscribe(fishes => {
      this.fishes = fishes;
      this.refreshEvent.emit(null);
    });
    this.scheduleService.getAll().subscribe(schedules => {
      this.schedules = schedules;
      this.refreshEvent.emit(null);
    });
    this.staffService.getAll().subscribe(staffs => {
      this.staffs = staffs;
      this.refreshEvent.emit(null);
    });
  }

  showComponent(position: number) {
    this.shown = position;
  }
}
