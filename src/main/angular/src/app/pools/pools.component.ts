import {Component, OnInit} from '@angular/core';
import {Pool} from "../model/Pool";
import {PoolService} from "../service/pool.service";
import {Specie} from "../model/Specie";

@Component({
  selector: 'app-pools',
  templateUrl: './pools.component.html',
  styleUrls: ['./pools.component.css']
})
export class PoolsComponent implements OnInit {
  pools: Array<Pool>

  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Specie) {
    this.poolService.getAll().subscribe(
      data => {
        this.pools = data;
      },
      error => console.log(error)
    );

  }

}
