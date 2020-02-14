import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitiesCreatorComponent } from './activities-creator.component';

describe('ActivitiesComponent', () => {
  let component: ActivitiesCreatorComponent;
  let fixture: ComponentFixture<ActivitiesCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivitiesCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivitiesCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
