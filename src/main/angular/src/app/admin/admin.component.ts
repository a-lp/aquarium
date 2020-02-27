import {Component, OnInit} from '@angular/core';
import {SpeciesService} from '../service/species.service';
import {PoolService} from '../service/pool.service';
import {FishService} from '../service/fish.service';
import {ScheduleService} from '../service/schedule.service';
import {StaffService} from '../service/staff.service';
import {SectorService} from '../service/sector.service';
import {ActivityService} from '../service/activity.service';
import {AuthenticationService} from '../service/authentication.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  shown = 0;

  constructor(private fishService: FishService, private speciesService: SpeciesService,
              private poolService: PoolService, private scheduleService: ScheduleService,
              private staffService: StaffService, private sectorService: SectorService,
              private activityService: ActivityService, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    if (!this.authenticationService.isLogged()) {
      this.authenticationService.redirect('/login');
    }
  }
}
