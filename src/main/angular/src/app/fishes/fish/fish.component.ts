import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Fish} from "../../model/Fish";
import {FishService} from "../../service/fish.service";

@Component({
  selector: 'app-fish',
  templateUrl: './fish.component.html',
  styleUrls: ['./fish.component.css']
})
export class FishComponent implements OnInit {
  @Input()
  fish: Fish;
  @Output()
  retireEvent: EventEmitter<Fish> = new EventEmitter<Fish>();

  constructor(private fishService: FishService) {

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
