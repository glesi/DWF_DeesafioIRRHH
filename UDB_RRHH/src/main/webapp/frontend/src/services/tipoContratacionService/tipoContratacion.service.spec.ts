/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { TipoContratacionService } from './tipoContratacion.service';

describe('Service: TipoContratacion', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TipoContratacionService]
    });
  });

  it('should ...', inject([TipoContratacionService], (service: TipoContratacionService) => {
    expect(service).toBeTruthy();
  }));
});
