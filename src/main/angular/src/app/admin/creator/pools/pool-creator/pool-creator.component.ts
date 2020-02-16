import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Pool, WaterCondition} from "../../../../model/Pool";
import {Sector} from "../../../../model/Sector";
import {PoolService} from "../../../../service/pool.service";

@Component({
  selector: 'app-pool-creator',
  templateUrl: './pool-creator.component.html',
  styleUrls: ['./pool-creator.component.css']
})
export class PoolCreatorComponent implements OnInit {
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
        if (pool != null) {
          this.form.reset();
        }
      }
    );
  }
}
