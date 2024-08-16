import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

  API_URL : string = 'http://localhost:8080/UDB_RRHH/empleados';

  constructor(private httpClient : HttpClient) { }

  get(): Observable<any> {
    return this.httpClient.get(this.API_URL).pipe(res => res);
  }

  post(object : any): Observable<any> {
    return this.httpClient.post(this.API_URL, object).pipe(res => res);
  }

}
