import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AnimalsComponent} from './animals/animals.component';
import {AnimalsCreatorComponent} from './creator/animals-creator/animals-creator.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {AnimalComponent} from './animals/animal/animal.component';
import { SpeciesComponent } from './species/species.component';
import { SpeciesCreatorComponent } from './creator/species-creator/species-creator.component';
import { FormCreatorComponent } from './creator/form-creator.component';

@NgModule({
  declarations: [
    AppComponent,
    AnimalsComponent,
    AnimalsCreatorComponent,
    AnimalComponent,
    SpeciesComponent,
    SpeciesCreatorComponent,
    FormCreatorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
