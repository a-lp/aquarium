import {Component, Input, OnInit} from '@angular/core';
import {Sector} from '../../model/Sector';

@Component({
  selector: 'app-sector',
  templateUrl: './sector.component.html',
  styleUrls: ['./sector.component.css']
})
export class SectorComponent implements OnInit {
  @Input()
  sector: Sector;

  constructor() {
  }

  ngOnInit() {
  }

}
