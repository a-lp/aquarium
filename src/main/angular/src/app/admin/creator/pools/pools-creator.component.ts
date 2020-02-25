import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Sector} from '../../../model/Sector';
import {Pool, WaterCondition} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';
import {Specie} from '../../../model/Specie';
import {Staff} from '../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-pools-creator',
  templateUrl: './pools-creator.component.html',
  styleUrls: ['./pools-creator.component.css']
})
export class PoolsCreatorComponent implements OnInit {
  form = new FormGroup({
    maxCapacity: new FormControl('', Validators.required),
    volume: new FormControl('', Validators.required),
    sector: new FormControl('', Validators.required),
    responsible: new FormControl('', Validators.required)
  });

  @Input()
  sectors: Array<Sector>;
  @Input()
  staffs: Array<Staff>;
  @Input()
  pools: Array<Pool>;

  @Output()
  onChange: EventEmitter<Pool> = new EventEmitter<Pool>();
  pool: Pool;


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



  save($event: Event) {
    this.poolService.save(this.form.value).subscribe(pool => {
        this.onChange.emit(pool);
        if (pool != null) {
          this.form.reset();
        }
      }
    );
  }


}
