import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SectorCreatorComponent } from './sector-creator.component';

describe('SectorCreatorComponent', () => {
  let component: SectorCreatorComponent;
  let fixture: ComponentFixture<SectorCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SectorCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SectorCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
