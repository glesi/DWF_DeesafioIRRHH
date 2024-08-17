import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CRUDContratosComponent } from './crud-contratos.component';

describe('CRUDContratosComponent', () => {
  let component: CRUDContratosComponent;
  let fixture: ComponentFixture<CRUDContratosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CRUDContratosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CRUDContratosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
