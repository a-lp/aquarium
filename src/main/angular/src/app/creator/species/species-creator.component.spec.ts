import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpeciesCreatorComponent } from './species-creator.component';

describe('SpeciesCreatorComponent', () => {
  let component: SpeciesCreatorComponent;
  let fixture: ComponentFixture<SpeciesCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpeciesCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpeciesCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
