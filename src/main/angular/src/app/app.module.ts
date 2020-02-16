import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FishesComponent} from './fishes/fishes.component';
import {FishesCreatorComponent} from './admin/creator/fishes/fishes-creator.component';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {FishComponent} from './fishes/fish/fish.component';
import {SpeciesComponent} from './species/species.component';
import {SpeciesCreatorComponent} from './admin/creator/species/species-creator.component';
import {PoolsCreatorComponent} from './admin/creator/pools/pools-creator.component';
import {DatePipe, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {RouterModule} from '@angular/router';
import {AdminComponent} from './admin/admin.component';
import {SpecieComponent} from './species/specie/specie.component';
import {PoolsComponent} from './pools/pools.component';
import {PoolComponent} from './pools/pool/pool.component';
import {ActivitiesCreatorComponent} from './admin/creator/activities/activities-creator.component';
import {SchedulesCreatorComponent} from './admin/creator/schedules/schedules-creator.component';
import {StaffsCreatorComponent} from './admin/creator/staffs/staffs-creator.component';
import {SchedulesComponent} from './schedules/schedules.component';
import {ScheduleComponent} from './schedules/schedule/schedule.component';
import {StaffsComponent} from './staffs/staffs.component';
import {StaffComponent} from './staffs/staff/staff.component';
import {SectorsCreatorComponent} from './admin/creator/sectors/sectors-creator.component';
import {ActivityComponent} from './activities/activity/activity.component';
import {ActivitiesComponent} from './activities/activities.component';
import { SectorsComponent } from './sectors/sectors.component';
import { SectorComponent } from './sectors/sector/sector.component';
import { FishCreatorComponent } from './admin/creator/fishes/fish-creator/fish-creator.component';
import { SpecieCreatorComponent } from './admin/creator/species/specie-creator/specie-creator.component';
import { PoolCreatorComponent } from './admin/creator/pools/pool-creator/pool-creator.component';
import { SectorCreatorComponent } from './admin/creator/sectors/sector-creator/sector-creator.component';
import { StaffCreatorComponent } from './admin/creator/staffs/staff-creator/staff-creator.component';
import { ScheduleCreatorComponent } from './admin/creator/schedules/schedule-creator/schedule-creator.component';
import { ActivityCreatorComponent } from './admin/creator/activities/activity-creator/activity-creator.component';

@NgModule({
  declarations: [
    AppComponent,
    FishesComponent,
    FishesCreatorComponent,
    FishComponent,
    SpeciesComponent,
    SpeciesCreatorComponent,
    PoolsCreatorComponent,
    AdminComponent,
    SpecieComponent,
    PoolsComponent,
    PoolComponent,
    ActivitiesComponent,
    SchedulesCreatorComponent,
    StaffsCreatorComponent,
    SchedulesComponent,
    ScheduleComponent,
    StaffsComponent,
    StaffComponent,
    SectorsCreatorComponent,
    ActivityComponent,
    ActivitiesCreatorComponent,
    ActivitiesComponent,
    SectorsComponent,
    SectorComponent,
    FishCreatorComponent,
    SpecieCreatorComponent,
    PoolCreatorComponent,
    SectorCreatorComponent,
    StaffCreatorComponent,
    ScheduleCreatorComponent,
    ActivityCreatorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      {
        path: 'view-fishes',
        component: FishesComponent
      },
      {
        path: 'view-species',
        component: SpeciesComponent
      },
      {
        path: 'view-pools',
        component: PoolsComponent
      },
      {
        path: 'view-admin',
        component: AdminComponent
      }
    ], {useHash: true})
  ],
  providers: [DatePipe, {provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
