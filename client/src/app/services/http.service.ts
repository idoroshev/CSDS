import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IPublicKey } from '../interfaces/rsa';

export interface ISessionResponse {
  sessionKey: string;
  initVector: string;
}

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  serverUrl = 'http://192.168.0.12';
  port = 8080;

  constructor(private http: HttpClient) { }

  getFile(fileName: string, username: string): Observable<string> {
    return this.http.get(`${this.serverUrl}:${this.port}/files`, {
      params:  {
        name: fileName,
        username,
      },
      responseType: 'text',
    }).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError(e);
        }
      }),
    );
  }

  session(username: string, publicKey: IPublicKey): Observable<ISessionResponse> {
    return this.http.post<ISessionResponse>(
      `${this.serverUrl}:${this.port}/session`,
      { username, ...publicKey },
      { responseType: 'json' },
    ).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError(e);
        }
      }),
    );
  }

  login(username: string, encryptedPassword: string): Observable<any> {
    return this.http.post(
      `${this.serverUrl}:${this.port}/login`,
      { username, password: encryptedPassword },
    ).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError(e);
        }
      }),
    );
  }

  logout(): Observable<any> {
    return this.http.post(
      `${this.serverUrl}:${this.port}/logout`,
      null,
    ).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError(e);
        }
      }),
    );
  }
}
