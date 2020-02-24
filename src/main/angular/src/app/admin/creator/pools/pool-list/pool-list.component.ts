import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Pool} from "../../../../model/Pool";
import {PoolService} from "../../../../service/pool.service";

@Component({
  selector: 'app-pool-list',
  templateUrl: './pool-list.component.html',
  styleUrls: ['./pool-list.component.css']
})
export class PoolListComponent implements OnInit {
  @Input()
  pools: Array<Pool>;
  @Output()
  onSelect: EventEmitter<Pool> = new EventEmitter<Pool>();
  @Output()
  onChange: EventEmitter<Pool> = new EventEmitter<Pool>();

  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
  }

  removePool(pool: Pool) {
    //TODO: gestire errore di costraint
    this.poolService.remove(pool).subscribe(
      removedPool => {
        this.onChange.emit(removedPool);
      }, error => {
        console.log(error);
      }
    );
  }

  showPool(pool: Pool) {
    this.onSelect.emit(pool);
  }
}
