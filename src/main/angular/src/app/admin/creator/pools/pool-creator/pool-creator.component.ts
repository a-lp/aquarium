import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Pool, WaterCondition} from '../../../../model/Pool';
import {Sector} from '../../../../model/Sector';
import {PoolService} from '../../../../service/pool.service';
import {Staff} from '../../../../model/Staff';
import {Fish} from '../../../../model/Fish';
import {Schedule} from '../../../../model/Schedule';
import {AuthenticationService} from '../../../../service/authentication.service';

@Component({
  selector: 'app-pool-creator',
  templateUrl: './pool-creator.component.html',
  styleUrls: ['./pool-creator.component.css']
})
export class PoolCreatorComponent implements OnInit {
  form = new FormGroup({
    maxCapacity: new FormControl({value: '', disabled: !this.authenticationService.isAdmin()}, Validators.required),
    volume: new FormControl({value: '', disabled: !this.authenticationService.isAdmin()}, Validators.required),
    condition: new FormControl({value: '', disabled: (!this.authenticationService.isManager())}, Validators.required),
    sector: new FormControl({value: '', disabled: !this.authenticationService.isAdmin()}, Validators.required),
    responsible: new FormControl({value: '', disabled: !this.authenticationService.isAdmin()}, Validators.required)
  });

  conditions = Object.values(WaterCondition);
  @Output()
  onChange: EventEmitter<Pool> = new EventEmitter<Pool>();
  @Input()
  sectors: Array<Sector>;
  @Input()
  pool: Pool;
  @Input()
  staffs: Array<Staff>;
  @Output()
  onHide = new EventEmitter();
  @Output()
  onError = new EventEmitter<any>();

  fishList: Array<Fish> = [];
  schedules: Array<Schedule> = [];

  constructor(private poolService: PoolService, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.getFishes();
    this.getSchedules();
  }

  hidePool() {
    this.onHide.emit();
  }

  update() {
    this.form.value.sector = this.sectors.filter(x => x.name == this.form.value.sector).map(x => x.id)[0];
    this.poolService.update(this.pool.id, this.form.value).subscribe(
      updatePool => {
        this.onChange.emit();
        this.onError.emit({error: {message: 'Correctly updated'}});
      }
      , error => this.onError.emit(error)
    );
  }

  getFishes() {
    this.poolService.getFishes(this.pool.id).subscribe(fishes => {
      this.fishList = fishes;
    }, error => this.onError.emit(error));
  }

  getSchedules() {
    this.poolService.getSchedules(this.pool.id).subscribe(schedules => {
      this.schedules = schedules;
    }, error => this.onError.emit(error));
  }
}
