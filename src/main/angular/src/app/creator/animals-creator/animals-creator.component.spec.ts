import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnimalsCreatorComponent } from './animals-creator.component';

describe('CreatorComponent', () => {
  let component: AnimalsCreatorComponent;
  let fixture: ComponentFixture<AnimalsCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnimalsCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnimalsCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
