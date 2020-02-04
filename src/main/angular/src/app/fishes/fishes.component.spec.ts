import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FishesComponent } from './fishes.component';

describe('FishComponent', () => {
  let component: FishesComponent;
  let fixture: ComponentFixture<FishesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FishesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FishesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
