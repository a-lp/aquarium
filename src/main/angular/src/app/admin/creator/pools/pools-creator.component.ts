import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Pool, WaterCondition} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';
import {Sector} from "../../../model/Sector";

@Component({
  selector: 'app-pools-creator',
  templateUrl: './pools-creator.component.html',
  styleUrls: ['./pools-creator.component.css']
})
export class PoolsCreatorComponent implements OnInit {
  form = new FormGroup({
    maxCapacity: new FormControl('', Validators.required),
    volume: new FormControl('', Validators.required),
    condition: new FormControl('', Validators.required),
    sector: new FormControl('', Validators.required)
  });
  conditions = Object.values(WaterCondition);
  @Output()
  onSave: EventEmitter<Pool> = new EventEmitter<Pool>();
  @Input()
  sectors: Array<Sector>;

  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.poolService.save(this.form.value).subscribe(pool => {
        this.onSave.emit(pool);
        if (pool != null) this.form.reset();
      }
    );
  }
}
