import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulesCreatorComponent } from './schedules-creator.component';

describe('ScheduleComponent', () => {
  let component: SchedulesCreatorComponent;
  let fixture: ComponentFixture<SchedulesCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchedulesCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedulesCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
