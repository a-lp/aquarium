import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';
import {Specie} from '../../../model/Specie';

@Component({
  selector: 'app-pools-creator',
  templateUrl: './pools-creator.component.html',
  styleUrls: ['./pools-creator.component.css']
})
export class PoolsCreatorComponent implements OnInit {
  @Input()
  sectors: Array<Sector>;
  @Input()
  pools: Array<Pool>;

  @Output()
  onChange: EventEmitter<Pool> = new EventEmitter<Pool>();


  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  onSavePool(pool: Pool) {
    this.refresh(null);
    this.onChange.emit(pool);
  }

  refresh($event: Specie) {
    this.poolService.getAll().subscribe(
      data => {
        this.pools = data;
      },
      error => console.log(error)
    );
  }

  removePool(pool: Pool) {
    this.poolService.remove(pool).subscribe(
      removedPool => {
        this.refresh(removedPool);
        this.onChange.emit(removedPool);
      }, error => {
        console.log(error);
      }
    )
  }
}
