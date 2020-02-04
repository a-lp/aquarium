import {Component, OnInit} from '@angular/core';
import {Fish} from '../model/Fish';
import {FishService} from '../service/fish.service';

@Component({
  selector: 'app-fishes',
  templateUrl: './fishes.component.html',
  styleUrls: ['./fishes.component.css']
})
export class FishesComponent implements OnInit {
  fishes: Array<Fish>;
  retiredFishes: Array<Fish>;

  constructor(private animalService: FishService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Fish) {
    this.animalService.getAll().subscribe(
      data => {
        if (data != null) {
          this.retiredFishes = data.filter(element => {
            return element.returnDate != null;
          });
          this.fishes = data.filter(element => {
            return !this.retiredFishes.includes(element);
          });
        }
      },
      error => console.log(error)
    );

  }

}
