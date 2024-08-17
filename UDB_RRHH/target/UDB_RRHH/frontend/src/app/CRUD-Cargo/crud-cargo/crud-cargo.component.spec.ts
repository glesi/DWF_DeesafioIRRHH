import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CRUDCargoComponent } from './crud-cargo.component';

describe('CRUDCargoComponent', () => {
  let component: CRUDCargoComponent;
  let fixture: ComponentFixture<CRUDCargoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CRUDCargoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CRUDCargoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
