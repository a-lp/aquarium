import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {FishGender} from '../../../model/FishGender';
import {Specie} from '../../../model/Specie';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from '../../../service/pool.service';
import {Pool} from "../../../model/Pool";


@Component({
  selector: 'app-fishes-creator',
  templateUrl: './fishes-creator.component.html',
  styleUrls: ['./fishes-creator.component.css']
})
export class FishesCreatorComponent implements OnInit {
  @Output()
  onSave: EventEmitter<Fish> = new EventEmitter<Fish>();
  genders = Object.values(FishGender);
  @Input()
  species: Array<Specie>;
  @Input()
  pools: Array<Pool>;

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });


  constructor(private fishService: FishService, private speciesService: SpeciesService, private poolService: PoolService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.fishService.save(this.form.value).subscribe(fish => {
      this.onSave.emit(fish);
    })
  }
}
