import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PoolsCreatorComponent } from './pools-creator.component';

describe('PoolsCreatorComponent', () => {
  let component: PoolsCreatorComponent;
  let fixture: ComponentFixture<PoolsCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoolsCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoolsCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
