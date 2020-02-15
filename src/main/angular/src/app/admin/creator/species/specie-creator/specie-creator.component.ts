import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SpeciesService} from '../../../../service/species.service';
import {Specie} from '../../../../model/Specie';
import {Alimentation} from '../../../../model/Alimentation';

@Component({
  selector: 'app-specie-creator',
  templateUrl: './specie-creator.component.html',
  styleUrls: ['./specie-creator.component.css']
})
export class SpecieCreatorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl(''),
    extinctionLevel: new FormControl(''),
    alimentation: new FormControl('')
  });

  alimentations = Object.values(Alimentation);
  @Output()
  onSave: EventEmitter<Specie> = new EventEmitter<Specie>();

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
  }

  save() {
    this.speciesService.save(this.form.value).subscribe(specie => {
      if (specie != null) {
        this.form.reset();
        this.onSave.emit();
      }
    });
  }
}
