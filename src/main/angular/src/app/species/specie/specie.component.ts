import {Component, Input, OnInit} from '@angular/core';
import {Specie} from '../../model/Specie';

@Component({
  selector: 'tr [app-specie]',
  templateUrl: './specie.component.html',
  styleUrls: ['./specie.component.css']
})
export class SpecieComponent implements OnInit {
  @Input()
  specie: Specie;

  constructor() {
  }

  ngOnInit() {
  }

}
