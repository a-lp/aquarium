import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Specie} from '../model/Specie';
import {Pool} from '../model/Pool';
import {SpeciesService} from '../service/species.service';
import {PoolService} from '../service/pool.service';
import {Fish} from '../model/Fish';
import {FishService} from '../service/fish.service';
import {ScheduleService} from '../service/schedule.service';
import {Schedule} from '../model/Schedule';
import {StaffService} from '../service/staff.service';
import {Staff} from '../model/Staff';
import {Sector} from '../model/Sector';
import {SectorService} from '../service/sector.service';
import {PoolActivity} from '../model/PoolActivity';
import {ActivityService} from '../service/activity.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  fishes: Array<Fish> = [];
  species: Array<Specie> = [];
  pools: Array<Pool> = [];
  staffs: Array<Staff>;
  sectors: Array<Sector> = [];
  schedules: Array<Schedule> = [];
  activities: Array<PoolActivity> = [];

  @ViewChild('components', {static: false})
  components: ElementRef;
  shown = 1;

  constructor(private fishService: FishService, private speciesService: SpeciesService,
              private poolService: PoolService, private scheduleService: ScheduleService,
              private staffService: StaffService, private sectorService: SectorService,
              private activityService: ActivityService) {
  }

  ngOnInit() {
    this.refresh();

  }

  refresh() {
    this.speciesService.getAll().subscribe(data => {
      this.species = data;
    });
    this.poolService.getAll().subscribe(pools => {
      this.pools = pools;
    });
    this.fishService.getAll().subscribe(fishes => {
      this.fishes = fishes;
    });
    this.scheduleService.getAll().subscribe(schedules => {
      this.schedules = schedules;
    });
    this.staffService.getAll().subscribe(staffs => {
      this.staffs = staffs;
    });
    this.sectorService.getAll().subscribe(sectors => {
      this.sectors = sectors;
    });
    this.activityService.getAll().subscribe(activities => {
        this.activities = activities;
      }
    );
  }

  showComponent(position: number) {
    this.shown = position;
  }

}
