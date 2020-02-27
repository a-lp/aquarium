import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Fish} from '../../../../model/Fish';
import {FishService} from '../../../../service/fish.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-fish-list',
  templateUrl: './fish-list.component.html',
  styleUrls: ['./fish-list.component.css']
})
export class FishListComponent implements OnInit {
  @Input()
  fishes: Array<Fish>;
  @Output()
  onChange = new EventEmitter();
  @Output()
  onError = new EventEmitter<any>();
  @Output()
  onSelection = new EventEmitter<Fish>();
  field = 0; // Sorting field
  ascendent = true;

  constructor(private fishService: FishService, private datePipe: DatePipe) {
  }

  ngOnInit() {
  }

  retireAnimal(fish: Fish) {
    this.fishService.retireFish(fish).subscribe(
      data => {
        this.onChange.emit(fish);
      }, error => this.onError.emit(error)
    );
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

  selectFish(fish: Fish) {
    this.onSelection.emit(fish);
  }

  removeFish(fish: Fish) {
    this.fishService.delete(fish).subscribe(
      removedFish => {
        this.onChange.emit(removedFish);
      }, error => this.onError.emit(error)
    );
  }
}
