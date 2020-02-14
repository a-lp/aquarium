import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Alimentation} from '../../../model/Alimentation';
import {SpeciesService} from '../../../service/species.service';
import {Specie} from '../../../model/Specie';

@Component({
  selector: 'app-species-creator',
  templateUrl: './species-creator.component.html',
  styleUrls: ['./species-creator.component.css']
})
export class SpeciesCreatorComponent implements OnInit {
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    lifeSpan: new FormControl(''),
    extinctionLevel: new FormControl(''),
    alimentation: new FormControl('')
  });
  @Output()
  onSave: EventEmitter<Specie> = new EventEmitter<Specie>();

  alimentations = Object.values(Alimentation);

  constructor(private speciesService: SpeciesService) {
  }

  ngOnInit() {
  }

  save($event: Event) {
    this.speciesService.save(this.form.value).subscribe(specie => {
      this.onSave.emit();
      if (specie != null) { this.form.reset(); }
    });
  }
}
