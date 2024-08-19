import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContratacionService {

  API_URL : string = 'http://localhost:8080/UDB_RRHH/contrataciones';

  constructor(private httpClient : HttpClient) { }

  get(path: string): Observable<any> {
    return this.httpClient.get(this.API_URL+path).pipe(res => res);
  }

  post(path:string, object : any): Observable<any> {
    return this.httpClient.post(this.API_URL+path, object).pipe(res => res);
  }

}
