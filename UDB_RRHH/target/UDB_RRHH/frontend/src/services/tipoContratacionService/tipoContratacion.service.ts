import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TipoContratacionService {

  object : any = {
    "json": {
      "idTipoContratacion": 2,
      "tipoContratacion": "Medio Tiempo"
    },
    "action": "insertar"
  }

  API_URL : string = 'http://localhost:8080/UDB_RRHH/tipoContrataciones';

  constructor(private httpClient : HttpClient) { }

  get(): Observable<any> {
    return this.httpClient.get(this.API_URL).pipe(res => res);
  }

  post(): Observable<any> {
    return this.httpClient.post(this.API_URL, this.object).pipe(res => res);
  }

}
