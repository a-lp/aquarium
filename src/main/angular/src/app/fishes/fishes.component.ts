import {Component, Input, OnInit} from '@angular/core';
import {Fish} from '../model/Fish';
import {FishService} from '../service/fish.service';

@Component({
  selector: 'app-fishes',
  templateUrl: './fishes.component.html',
  styleUrls: ['./fishes.component.css']
})
export class FishesComponent implements OnInit {
  @Input()
  fishes: Array<Fish>;
  field: number = 0; // Sorting field
  ascendent: boolean = true;

  // retiredFishes: Array<Fish>;

  constructor(private animalService: FishService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Fish) {
    this.animalService.getAll().subscribe(
      data => {
        if (data != null) {
          this.fishes = data;
          // this.retiredFishes = data.filter(element => {
          //   return element.returnDate != null;
          // });
          // this.fishes = data.filter(element => {
          //   return !this.retiredFishes.includes(element);
          // });
        }
      },
      error => console.log(error)
    );
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
          console.log(a, b)
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
}
