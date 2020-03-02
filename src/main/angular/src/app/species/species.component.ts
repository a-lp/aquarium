import {Component, OnInit} from '@angular/core';
import {Specie} from '../model/Specie';
import {SpeciesService} from '../service/species.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../model/Alimentation';

@Component({
  selector: 'app-species',
  templateUrl: './species.component.html',
  styleUrls: ['./species.component.css']
})
export class SpeciesComponent implements OnInit {
  species: Array<Specie> = [];
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl(''),
    extinctionLevel: new FormControl(''),
    alimentation: new FormControl('')
  });
  timeout: any;
  alimentations = Object.values(Alimentation);

  constructor(private speciesService: SpeciesService) {
    this.form.valueChanges.subscribe(change => {
      if (this.timeout != null) {
        clearTimeout(this.timeout);
      }
      this.timeout = setTimeout(() => {
        this.speciesService.getAll().subscribe(
          data => {
            if (data != null) {
              this.species = data;
              for (const key of Object.keys(this.form.value)) {
                if (this.form.value[key] != null && this.form.value[key] != '') {
                  this.species = this.species.filter(x => {
                    if (key == 'name') {
                      return x.name.includes(this.form.value[key]);
                    }
                    return x[key] == this.form.value[key];
                  });
                }
              }
            }
          }
        );
      }, 500);
    });
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.speciesService.getAll().subscribe(
      data => {
        if (data != null) {
          this.species = data;
        }
      }
    );
  }
}
