import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecieCreatorComponent } from './specie-creator.component';

describe('SpecieCreatorComponent', () => {
  let component: SpecieCreatorComponent;
  let fixture: ComponentFixture<SpecieCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpecieCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecieCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
