import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FishesComponent} from './fishes/fishes.component';
import {FishesCreatorComponent} from './creator/fishes/fishes-creator.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {FishComponent} from './fishes/fish/fish.component';
import { SpeciesComponent } from './species/species.component';
import { SpeciesCreatorComponent } from './creator/species/species-creator.component';
import { FormCreatorComponent } from './creator/form-creator.component';
import { PoolsCreatorComponent } from './creator/pools/pools-creator/pools-creator.component';
import {DatePipe} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    FishesComponent,
    FishesCreatorComponent,
    FishComponent,
    SpeciesComponent,
    SpeciesCreatorComponent,
    FormCreatorComponent,
    PoolsCreatorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule {
}
