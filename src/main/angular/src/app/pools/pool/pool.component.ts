import {Component, Input, OnInit} from '@angular/core';
import {Pool} from '../../model/Pool';

@Component({
  selector: 'app-pool',
  templateUrl: './pool.component.html',
  styleUrls: ['./pool.component.css']
})
export class PoolComponent implements OnInit {
  @Input()
  pool: Pool;

  constructor() {
  }

  ngOnInit() {
  }

}
