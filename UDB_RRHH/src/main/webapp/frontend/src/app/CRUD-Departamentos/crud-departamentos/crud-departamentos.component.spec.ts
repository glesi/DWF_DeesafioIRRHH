import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CRUDDepartamentosComponent } from './crud-departamentos.component';

describe('CRUDDepartamentosComponent', () => {
  let component: CRUDDepartamentosComponent;
  let fixture: ComponentFixture<CRUDDepartamentosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CRUDDepartamentosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CRUDDepartamentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
