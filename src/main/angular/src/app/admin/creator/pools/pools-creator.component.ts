import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Pool, WaterCondition} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';

@Component({
  selector: 'app-pools-creator',
  templateUrl: './pools-creator.component.html',
  styleUrls: ['./pools-creator.component.css']
})
export class PoolsCreatorComponent implements OnInit {
  profileForm = new FormGroup({
    maxCapacity: new FormControl('', Validators.required),
    volume: new FormControl('', Validators.required),
    condition: new FormControl('', Validators.required),
  });
  conditions = Object.values(WaterCondition);
  @Output()
  onSave: EventEmitter<Pool> = new EventEmitter<Pool>();

  constructor(private poolService: PoolService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.poolService.save(this.profileForm.value).subscribe(value => {
        this.onSave.emit(value);
      }
    );
  }
}
