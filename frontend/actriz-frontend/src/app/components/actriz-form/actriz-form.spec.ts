import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActrizForm } from './actriz-form';

describe('ActrizForm', () => {
  let component: ActrizForm;
  let fixture: ComponentFixture<ActrizForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActrizForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActrizForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
