import {Component, OnInit} from '@angular/core';
import {FishService} from '../service/fish.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../model/Fish';
import {FishGender} from '../model/FishGender';
import {Specie} from '../model/Specie';
import {Pool} from '../model/Pool';
import {SpeciesService} from '../service/species.service';
import {PoolService} from '../service/pool.service';

@Component({
  selector: 'app-fishes',
  templateUrl: './fishes.component.html',
  styleUrls: ['./fishes.component.css']
})
export class FishesComponent implements OnInit {
  fishes: Array<Fish> = [];
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });
  genders = Object.values(FishGender);
  timeout: any;
  species: Array<Specie> = [];
  pools: Array<Pool> = [];

  constructor(private fishService: FishService, private speciesService: SpeciesService,
              private poolService: PoolService) {
    this.form.valueChanges.subscribe(change => {
      if (this.timeout != null) {
        clearTimeout(this.timeout);
      }
      this.timeout = setTimeout(() => {
        this.fishService.getAll().subscribe(
          data => {
            if (data != null) {
              this.fishes = data;
              for (const key of Object.keys(this.form.value)) {
                if (this.form.value[key] != null && this.form.value[key] != '') {
                  this.fishes = this.fishes.filter(x => {
                    if (key == 'name' || key == 'distinctSign') {
                      return x[key].includes(this.form.value[key]);
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
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      });
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
        }
      }
    );
  }
}
