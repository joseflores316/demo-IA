import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActrizList } from './actriz-list';

describe('ActrizList', () => {
  let component: ActrizList;
  let fixture: ComponentFixture<ActrizList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActrizList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActrizList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
