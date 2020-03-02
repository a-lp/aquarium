import {Component, EventEmitter, OnInit} from '@angular/core';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';
import {Staff} from '../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SectorService} from '../../../service/sector.service';
import {StaffService} from '../../../service/staff.service';
import {StaffRole} from "../../../model/StaffRole";
import {AuthenticationService} from "../../../service/authentication.service";

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
  onError = new EventEmitter<string>();
  sectors: Array<Sector> = [];
  staffs: Array<Staff> = [];
  pools: Array<Pool> = [];

  pool: Pool;

  filters = new FormGroup({
    responsible: new FormControl({value: '', disabled: !this.authenticationService.isAdmin()}),
    sector: new FormControl('')
  })

  constructor(private poolService: PoolService, private sectorService: SectorService,
              private staffService: StaffService, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.refresh();
  }

  onSavePool(pool: Pool) {
    this.refresh();
  }

  refresh() {
    this.poolService.getAllByResponsible().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
    this.sectorService.getAll().subscribe(
      data => {
        if (data != null) {
          this.sectors = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
    this.staffService.getByRole(StaffRole.MANAGER).subscribe(
      data => {
        if (data != null) {
          this.staffs = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
  }


  save() {
    this.poolService.save(this.form.value).subscribe(pool => {
        if (pool != null) {
          this.form.reset();
          this.refresh();
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  filterPools() {
    this.poolService.getAllByResponsible().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
          for (const key of Object.keys(this.filters.value))
            if (this.filters.value[key] != '')
              this.pools = this.pools.filter(x => x[key] == this.filters.value[key]);
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  resetFilters() {
    this.filters.reset();
    this.poolService.getAllByResponsible().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }, error => this.onError.emit(error.error.message)
    );
  }

  emitError(error: any) {
    console.log(error);
    this.onError.emit(error.error.message);
  }
}
