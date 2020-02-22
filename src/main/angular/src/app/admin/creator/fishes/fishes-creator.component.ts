import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from '../../../service/pool.service';
import {DatePipe} from '@angular/common';
import {Specie} from '../../../model/Specie';
import {Pool} from '../../../model/Pool';
import {FishGender} from "../../../model/FishGender";


@Component({
  selector: 'app-fishes-creator',
  templateUrl: './fishes-creator.component.html',
  styleUrls: ['./fishes-creator.component.css']
})
export class FishesCreatorComponent implements OnInit {
  @Input()
  species: Array<Specie>;
  @Input()
  pools: Array<Pool>;
  @Output()
  onChange: EventEmitter<Fish> = new EventEmitter<Fish>();
  @Input()
  fishes: Array<Fish>;
  genders = Object.values(FishGender);
  field = 0; // Sorting field
  ascendent = true;
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
    this.refresh(null);
  }

  sort(field: number) {
    if (this.field == field) {
      this.ascendent = !this.ascendent;
    } else {
      this.ascendent = true;
    }
    this.field = field;
    this.fishService.sort(this.fishes, this.field, this.ascendent);
  }

  refresh($event: Fish) {
    this.fishService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
        }
      },
      error => console.log(error)
    );
  }

  retireAnimal(fish: Fish) {
    this.fishService.retireFish(fish).subscribe(
      data => {
        this.refresh(data);
        this.onChange.emit(fish);
      },
      error => console.log(error)
    );
  }

  onSaveFish(fish: Fish) {
    this.refresh(fish);
    this.onChange.emit(fish);
  }

  removeFish(fish: Fish) {
    this.fishService.delete(fish).subscribe(
      removedFish => {
        this.refresh(removedFish);
        this.onChange.emit(removedFish);
      }
    );
  }

  save($event: Event) {
    this.fishService.save(this.form.value).subscribe(fish => {
      this.onChange.emit(fish);
      this.form.reset();
    });
  }

  selectFish(fish: Fish) {
    this.fish = fish;
  }
}
