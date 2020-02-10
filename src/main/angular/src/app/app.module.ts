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
import {ActivitiesComponent} from './admin/creator/activities/activities.component';
import {ScheduleCreatorComponent} from './admin/creator/schedule/schedule-creator.component';
import {StaffCreatorComponent} from './admin/creator/staff/staff-creator.component';
import {SectorComponent} from './admin/creator/sector/sector.component';
import {SchedulesComponent} from './schedules/schedules.component';
import {ScheduleComponent} from './schedules/schedule/schedule.component';

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
    ScheduleCreatorComponent,
    StaffCreatorComponent,
    SectorComponent,
    SchedulesComponent,
    ScheduleComponent
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
