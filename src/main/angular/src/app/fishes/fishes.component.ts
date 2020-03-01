import {Component, OnInit} from '@angular/core';
import {Fish} from "../model/Fish";
import {FishService} from "../service/fish.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-fishes',
  templateUrl: './fishes.component.html',
  styleUrls: ['./fishes.component.css']
})
export class FishesComponent implements OnInit {
  fishes: Array<Fish> = [];

  constructor(private fishService: FishService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.fishService.getAll().subscribe(
      data => {
        console.log(data);
        if (data != null) {
          this.fishes = data;
        }
      }
    );
  }
}
