import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from '../../../service/pool.service';
import {DatePipe} from '@angular/common';
import {Specie} from '../../../model/Specie';
import {Pool} from '../../../model/Pool';


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
  fishes: Array<Fish>;
  field = 0; // Sorting field
  ascendent = true;
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });


  constructor(private fishService: FishService, private speciesService: SpeciesService, private poolService: PoolService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  sort(field: number) {
    console.log(field);
    if (this.field == field) {
      this.ascendent = !this.ascendent;
    } else {
      this.ascendent = true;
    }
    this.field = field;
    switch (field) {
      case 0:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * a.id.toString().localeCompare(b.id.toString()));
        break;
      case 1:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * a.name.localeCompare(b.name));
        break;
      case 2:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * a.gender.localeCompare(b.gender));
        break;
      case 3:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * (a.arrivalDate == b.arrivalDate ? 0 : (a.arrivalDate > b.arrivalDate ? 1 : -1)));
        break;
      case 4:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * (a.returnDate == b.returnDate ? 0 : (a.returnDate > b.returnDate ? 1 : -1)));
        break;
      case 5:
        this.fishes = this.fishes.sort((a, b) => (this.ascendent ? 1 : -1) * a.specie.name.localeCompare(b.specie.name));
        break;
      case 6:
        this.fishes = this.fishes.sort((a, b) => {
          console.log(a, b);
          if (a.pool == null) {
            return (this.ascendent ? 1 : -1);
          }
          if (b.pool == null) {
            return (this.ascendent ? -1 : 1);
          }
          return (this.ascendent ? 1 : -1) * a.pool.id.toString().localeCompare(b.pool.id.toString());
        });
        break;
    }
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
}
