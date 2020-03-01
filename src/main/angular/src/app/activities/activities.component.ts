import {Component, OnInit} from '@angular/core';
import {ActivityService} from '../service/activity.service';
import {PoolActivity} from '../model/PoolActivity';
import {DatePipe} from '@angular/common';
import {FormControl, FormGroup} from '@angular/forms';
import {SectorService} from '../service/sector.service';
import {PoolService} from '../service/pool.service';
import {Pool} from '../model/Pool';
import {Sector} from '../model/Sector';

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.css']
})
export class ActivitiesComponent implements OnInit {
  activities: Array<PoolActivity> = [];
  pools: Array<Pool> = [];
  sectors: Array<Sector> = [];

  filters = new FormGroup({
    pool: new FormControl(''),
    sector: new FormControl(''),
    day: new FormControl(''),
    startActivity: new FormControl(''),
    endActivity: new FormControl('')
  });

  constructor(private activityService: ActivityService, private datePipe: DatePipe,
              private sectorService: SectorService, private poolService: PoolService) {
    this.filters.get('pool').valueChanges.subscribe(
      change => {
        if (change != null && change != '') {
          this.poolService.getActivities(change).subscribe(
            data => {
              this.activities = data.filter(x => x.openToPublic == true);
            }
          );
        }
      }
    );
    this.filters.get('sector').valueChanges.subscribe(
      change => {
        this.activityService.getAllBySector(change).subscribe(
          data => {
            this.activities = data.filter(x => x.openToPublic == true);
          }
        );
        this.poolService.getAll().subscribe(
          data => {
            this.pools = data;
            if (change != '') {
              this.pools = data.filter(x => x.sector == change);
            }
          }
        );
      }
    );
    this.filters.get('day').valueChanges.subscribe(
      change => {
        if (change != null && change != '') {
          this.activityService.getAllOpenToPublic().subscribe(
            data => {
              this.activities = data.filter(x => new Date(x.day).getTime() == new Date(change).getTime());
            }
          );
        }
      }
    );
    this.filters.get('startActivity').valueChanges.subscribe(
      change => {
        if (change != null && change != '') {
          this.activityService.getAllOpenToPublic().subscribe(
            data => {

              this.activities = data.filter(x => {
                return x.startActivity.substr(0, 5) == change
              });
            }
          );
        }
      }
    );
    this.filters.get('endActivity').valueChanges.subscribe(
      change => {
        if (change != null && change != '') {
          this.activityService.getAllOpenToPublic().subscribe(
            data => {

              this.activities = data.filter(x => {
                return x.endActivity.substr(0, 5) == change
              });
            }
          );
        }
      }
    );
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.activityService.getAllOpenToPublic().subscribe(
      data => {
        if (data != null) {
          this.activities = data;
        }
      }
    );
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }
    );
    this.sectorService.getAll().subscribe(
      data => {
        if (data != null) {
          this.sectors = data;
        }
      }
    );
  }

  selectActivity(activity: PoolActivity) {
    console.log(activity);
  }

  resetFilters() {
    this.filters.reset();
    this.refresh();
  }
}
