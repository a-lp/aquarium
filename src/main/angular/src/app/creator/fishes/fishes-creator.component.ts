import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Fish} from '../../model/Fish';
import {FishService} from '../../service/fish.service';
import {FishGender} from '../../model/FishGender';
import {Specie} from '../../model/Specie';
import {SpeciesService} from '../../service/species.service';


@Component({
  selector: 'app-fishes-creator',
  templateUrl: './fishes-creator.component.html',
  styleUrls: ['./fishes-creator.component.css']
})
export class FishesCreatorComponent implements OnInit {
  profileForm = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl('', Validators.required)
  });

  @Output()
  onSave: EventEmitter<Fish> = new EventEmitter<Fish>();
  genders = Object.values(FishGender);
  @Input()
  species: Array<Specie>;

  constructor(private fishService: FishService, private speciesService: SpeciesService) {
  }

  ngOnInit() {

  }

  save($event: Event) {
    this.speciesService.getSpecie(this.profileForm.value.specie).subscribe(specie => {
      this.profileForm.value.specie = specie;
      console.log(this.profileForm.value)
      this.fishService.save(this.profileForm.value).subscribe(
        data => {
          console.log(data)
          this.onSave.emit(data);
          this.profileForm.reset();
        },
        error => console.log(error)
      );
    });
  }
}
