import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FishCreatorComponent } from './fish-creator.component';

describe('FishCreatorComponent', () => {
  let component: FishCreatorComponent;
  let fixture: ComponentFixture<FishCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FishCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FishCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
