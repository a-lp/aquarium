import {Component, Input, OnInit} from '@angular/core';
import {Sector} from "../model/Sector";
import {Specie} from "../model/Specie";
import {SectorService} from "../service/sector.service";

@Component({
  selector: 'app-sectors',
  templateUrl: './sectors.component.html',
  styleUrls: ['./sectors.component.css']
})
export class SectorsComponent implements OnInit {
  @Input()
  sectors: Array<Sector>;

  constructor(private sectorService: SectorService) {
  }

  ngOnInit() {
    this.refresh(null);
  }

  refresh($event: Specie) {
    this.sectorService.getAll().subscribe(
      data => {
        this.sectors = data;
      },
      error => console.log(error)
    );

  }
}
