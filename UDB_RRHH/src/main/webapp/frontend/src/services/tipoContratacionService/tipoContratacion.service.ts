import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {constantes } from "../../app/constantes/constantes";


@Injectable({
  providedIn: 'root'
})
export class TipoContratacionService {

  object : any = {
    'json': {
      'idTipoContratacion': 3,
      'tipoContratacion': "Medio Tiempo"
    },
    'action': 'insert'
  }

  API_URL : string = 'http://localhost:8080/UDB_RRHH/tipoContrataciones';

  constructor(private httpClient : HttpClient) { }

  get(): Observable<any> {
    return this.httpClient.get(constantes.API_ENDPOINT_URL+constantes.METHODS.TIPOCONTRATACIONS).pipe(res => res);
  }

  post(obj :any) {
    return this.httpClient.post(this.API_URL, this.object).pipe(res => res);
    // return this.httpClient.post<any>(constantes.API_ENDPOINT_URL+constantes.METHODS.TIPOCONTRATACIONS, obj);
  }
}
