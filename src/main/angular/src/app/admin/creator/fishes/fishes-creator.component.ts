import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../../model/Fish';
import {FishService} from '../../../service/fish.service';
import {FishGender} from '../../../model/FishGender';
import {Specie} from '../../../model/Specie';
import {SpeciesService} from '../../../service/species.service';
import {PoolService} from "../../../service/pool.service";


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
  pools: Array<Specie>;
  defaulSpecie: string;

  profileForm = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null)
  });


  constructor(private fishService: FishService, private speciesService: SpeciesService, private poolService: PoolService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.speciesService.getSpecie(this.profileForm.value.specie).subscribe(specie => {
      this.profileForm.value.specie = specie;
      if (this.profileForm.value.pool != undefined) {
        this.poolService.getPool(this.profileForm.value.pool).subscribe(pool => {
          this.profileForm.value.pool = pool;
          this.fishService.save(this.profileForm.value).subscribe(
            data => {
              console.log(data);
              this.onSave.emit(data);
              this.profileForm.reset();
            },
            error => console.log(error)
          );
        });
      } else {
        this.fishService.save(this.profileForm.value).subscribe(
          data => {
            console.log(data);
            this.onSave.emit(data);
            this.profileForm.reset();
          },
          error => console.log(error)
        );
      }
    });
  }
}
