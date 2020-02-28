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
  @Output()
  onError: EventEmitter<string> = new EventEmitter<string>();

  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
  }

  removePool(pool: Pool) {
    this.poolService.remove(pool).subscribe(
      removedPool => {
        this.onChange.emit(removedPool);
      }, error => {
        this.onError.emit(error);
      }
    );
  }

  showPool(pool: Pool) {
    this.onSelect.emit(pool);
  }
}
