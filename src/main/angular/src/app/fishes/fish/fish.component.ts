import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Fish} from '../../model/Fish';
import {FishService} from '../../service/fish.service';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'tr [app-fish]',
  templateUrl: './fish.component.html',
  styleUrls: ['./fish.component.css']
})
export class FishComponent implements OnInit {
  @Input()
  fish: Fish;
  @Output()
  retireEvent: EventEmitter<Fish> = new EventEmitter<Fish>();

  constructor(private fishService: FishService, private datePipe: DatePipe) {

  }

  ngOnInit() {
  }

  retireAnimal() {
    this.fishService.retireFish(this.fish).subscribe(
      data => {
        this.retireEvent.emit();
      },
      error => console.log(error)
    );
  }
}
