import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FishesCreatorComponent } from './fishes-creator.component';

describe('CreatorComponent', () => {
  let component: FishesCreatorComponent;
  let fixture: ComponentFixture<FishesCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FishesCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FishesCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
