import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Specie} from '../../model/Specie';

@Component({
  selector: 'tr [app-specie]',
  templateUrl: './specie.component.html',
  styleUrls: ['./specie.component.css']
})
export class SpecieComponent implements OnInit {
  @Input()
  specie: Specie;
  @Output()
  show = new EventEmitter<Specie>();

  constructor() {
  }

  ngOnInit() {
  }

  showFishes() {
    this.show.emit(this.specie);
  }
}
