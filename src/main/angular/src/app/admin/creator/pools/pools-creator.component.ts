import {Component, OnInit} from '@angular/core';
import {Sector} from '../../../model/Sector';
import {Pool} from '../../../model/Pool';
import {PoolService} from '../../../service/pool.service';
import {Staff} from '../../../model/Staff';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SectorService} from '../../../service/sector.service';
import {StaffService} from '../../../service/staff.service';

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

  sectors: Array<Sector> = [];
  staffs: Array<Staff> = [];
  pools: Array<Pool> = [];

  pool: Pool;

  constructor(private poolService: PoolService, private sectorService: SectorService, private staffService: StaffService) {
  }

  ngOnInit() {
    this.refresh();
  }

  onSavePool(pool: Pool) {
    this.refresh();
  }

  refresh() {
    this.poolService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      },
      error => console.log(error)
    );
    this.sectorService.getAll().subscribe(
      data => {
        if (data != null) {
          this.sectors = data;
        }
      },
      error => console.log(error)
    );
    this.staffService.getAll().subscribe(
      data => {
        if (data != null) {
          this.staffs = data;
        }
      },
      error => console.log(error)
    );
  }


  save($event: Event) {
    this.poolService.save(this.form.value).subscribe(pool => {
        if (pool != null) {
          this.form.reset();
          this.refresh();
        }
      }
    );
  }
}
