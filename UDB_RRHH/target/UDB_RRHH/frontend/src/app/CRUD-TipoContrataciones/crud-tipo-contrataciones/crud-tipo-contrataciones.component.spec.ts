import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CRUDTipoContratacionesComponent } from './crud-tipo-contrataciones.component';

describe('CRUDTipoContratacionesComponent', () => {
  let component: CRUDTipoContratacionesComponent;
  let fixture: ComponentFixture<CRUDTipoContratacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CRUDTipoContratacionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CRUDTipoContratacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
