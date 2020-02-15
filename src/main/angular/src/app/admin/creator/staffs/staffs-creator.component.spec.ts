import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffsCreatorComponent } from './staffs-creator.component';

describe('StaffComponent', () => {
  let component: StaffsCreatorComponent;
  let fixture: ComponentFixture<StaffsCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffsCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffsCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
