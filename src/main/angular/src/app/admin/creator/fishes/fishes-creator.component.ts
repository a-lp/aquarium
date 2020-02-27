import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from '../../../service/pool.service';
import {DatePipe} from '@angular/common';
import {Specie} from '../../../model/Specie';
import {Pool} from '../../../model/Pool';
import {FishGender} from '../../../model/FishGender';


@Component({
  selector: 'app-fishes-creator',
  templateUrl: './fishes-creator.component.html',
  styleUrls: ['./fishes-creator.component.css']
})
export class FishesCreatorComponent implements OnInit {
  species: Array<Specie> = [];
  pools: Array<Pool> = [];
  fishes: Array<Fish>;
  genders = Object.values(FishGender);

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });
  fish: Fish = null;


  constructor(private fishService: FishService, private speciesService: SpeciesService, private poolService: PoolService, private datePipe: DatePipe) {
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
      },
      error => console.log(error)
    );
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      },
      error => console.log(error));
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
        }
      },
      error => console.log(error)
    );
  }

  onChangeFish(fish: Fish) {
    this.refresh();
  }

  save($event: Event) {
    this.fishService.save(this.form.value).subscribe(fish => {
      this.form.reset();
    });
  }

}
