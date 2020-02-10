import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffCreatorComponent } from './staff-creator.component';

describe('StaffComponent', () => {
  let component: StaffCreatorComponent;
  let fixture: ComponentFixture<StaffCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
