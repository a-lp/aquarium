import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SectorsCreatorComponent } from './sectors-creator.component';

describe('SectorsComponent', () => {
  let component: SectorsCreatorComponent;
  let fixture: ComponentFixture<SectorsCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SectorsCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SectorsCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
