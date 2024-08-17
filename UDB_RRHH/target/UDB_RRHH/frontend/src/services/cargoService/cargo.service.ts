import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CargoService {

  API_URL : string = 'http://localhost:8080/UDB_RRHH/cargo';

  constructor(private httpClient : HttpClient) { }

  get(path: String): Observable<any> {
    return this.httpClient.get(this.API_URL+path).pipe(res => res);
  }

  post(path:string, object : any): Observable<any> {
    return this.httpClient.post(this.API_URL+path, object).pipe(res => res);
  }

}
