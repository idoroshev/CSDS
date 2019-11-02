import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IPublicKey } from '../interfaces/rsa';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  serverUrl = 'http://192.168.0.12';
  port = 8080;

  constructor(private http: HttpClient) { }

  getFile(fileName: string): Observable<string> {
    return this.http.get(`${this.serverUrl}:${this.port}/files`, {
      params:  {
        name: fileName,
      },
      responseType: 'text',
    }).pipe(
      catchError(e => {
        return throwError(e);
      }),
    );
  }

  session(username: string, publicKey: IPublicKey): Observable<string> {
    return this.http.post(
      `${this.serverUrl}:${this.port}/session`,
      { username, ...publicKey },
      { responseType: 'text' },
    ).pipe(
      catchError(e => {
        return throwError(e);
      }),
    );
  }

  login(username: string, encryptedPassword: string): Observable<any> {
    return this.http.post(
      `${this.serverUrl}:${this.port}/login`,
      { username, password: encryptedPassword },
    ).pipe(
      catchError(e => {
        return throwError(e);
      }),
    );
  }
}
