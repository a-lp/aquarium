import {Component, EventEmitter, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from '../../../service/pool.service';
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

  onError = new EventEmitter<any>();

  constructor(private fishService: FishService, private speciesService: SpeciesService,
              private poolService: PoolService) {
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
      }, error => this.onError.emit(error.error.message)
    );
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }, error => this.onError.emit(error.error.message));
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  onChangeFish(fish: Fish) {
    this.refresh();
  }

  save() {
    this.fishService.save(this.form.value).subscribe(fish => {
      if (fish != null) {
        this.form.reset();
        this.refresh();
      }
    }, error => this.onError.emit(error.error.message));
  }

}
