import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FishGender} from '../../../../model/FishGender';
import {Specie} from '../../../../model/Specie';
import {Pool} from '../../../../model/Pool';
import {FishService} from '../../../../service/fish.service';
import {Fish} from '../../../../model/Fish';

@Component({
  selector: 'app-fish-creator',
  templateUrl: './fish-creator.component.html',
  styleUrls: ['./fish-creator.component.css']
})
export class FishCreatorComponent implements OnInit {
  @Output()
  onHide: EventEmitter<any> = new EventEmitter<any>();
  @Output()
  onError: EventEmitter<any> = new EventEmitter<any>();
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    distinctSign: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    specie: new FormControl(null, Validators.required),
    pool: new FormControl(null, Validators.required)
  });
  genders = Object.values(FishGender);
  @Input()
  species: Array<Specie>;
  @Input()
  pools: Array<Pool>;
  @Input()
  fish: Fish;
  @Output()
  onSave: EventEmitter<Fish> = new EventEmitter<Fish>();

  constructor(private fishService: FishService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.fishService.save(this.form.value).subscribe(fish => {
      this.onSave.emit(fish);
      this.form.reset();
    }, error => this.onError.emit(error));
  }

  hideFish() {
    this.onHide.emit(null);
  }

  update() {
    this.fishService.update(this.fish.id, this.form.value).subscribe(
      fish => {
        this.onSave.emit();
      }, error => this.onError.emit(error)
    );
  }
}
