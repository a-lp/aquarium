import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PoolService} from '../service/pool.service';
import {Pool} from '../model/Pool';
import {Sector} from '../model/Sector';
import {SectorService} from "../service/sector.service";

@Component({
  selector: 'app-pools',
  templateUrl: './pools.component.html',
  styleUrls: ['./pools.component.css']
})
export class PoolsComponent implements OnInit {
  pools: Array<Pool> = [];
  sectors: Array<Sector> = [];
  form = new FormGroup({
    maxCapacity: new FormControl('', Validators.required),
    volume: new FormControl('', Validators.required),
    sector: new FormControl('', Validators.required)
  });
  timeout: any;

  constructor(private poolsService: PoolService, private sectorService: SectorService) {
    this.form.valueChanges.subscribe(change => {
      if (this.timeout != null) {
        clearTimeout(this.timeout);
      }
      this.timeout = setTimeout(() => {
        this.poolsService.getAll().subscribe(
          data => {
            if (data != null) {
              this.pools = data;
              for (const key of Object.keys(this.form.value)) {
                if (this.form.value[key] != null && this.form.value[key] != '') {
                  this.pools = this.pools.filter(x => {
                    return x[key] == this.form.value[key];
                  });
                }
              }
            }
          }
        );
      }, 500);
    });
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.poolsService.getAll().subscribe(
      data => {
        if (data != null) {
          this.pools = data;
        }
      }
    );
    this.sectorService.getAll().subscribe(data => {
      if (data != null) {
        this.sectors = data;
      }
    });
  }
}
