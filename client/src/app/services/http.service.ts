import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IPublicKey } from '../interfaces/rsa';
import { DataService } from './data.service';

import { utils, padding, ModeOfOperation } from 'aes-js';
import { decryptNextToken, encryptNextToken } from '../utils/index';

export interface ISessionResponse {
  sessionKey: string;
  initVector: string;
  nextToken: string;
}

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  serverUrl = 'http://192.168.0.12';
  port = 8080;

  constructor(
    private http: HttpClient,
    private dataService: DataService,
  ) { }

  getFile(fileName: string, username: string, decryptedNextToken: string): Observable<{ text: string, nextToken: string }> {
    return this.http.get<{ text: string, nextToken: string }>(`${this.serverUrl}:${this.port}/files`, {
      params:  {
        name: fileName,
        username,
        nextToken: decryptedNextToken,
      },
      responseType: 'json',
    }).pipe(
      catchError(e => {
        if (e.error) {
          if (JSON.parse(e.error.message).nextToken) {
            const dNextToken = decryptNextToken(
              this.dataService.getSessionKey(),
              this.dataService.getInitVector(),
              JSON.parse(e.error.message).nextToken,
            );
            const eNextToken = encryptNextToken(
              this.dataService.getSessionKey(),
              this.dataService.getInitVector(),
              dNextToken + username
            );
            this.dataService.setNextToken(eNextToken);
          }
          return throwError(JSON.parse(e.error.message).text);
        } else {
          return throwError('Something went wrong');
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
          return throwError('Something went wrong');
        }
      }),
    );
  }

  login(username: string, encryptedPassword: string, decryptedNextToken: string): Observable<{ nextToken: string }> {
    return this.http.post<{ nextToken: string }>(
      `${this.serverUrl}:${this.port}/login`,
      { username, password: encryptedPassword },
      {
        params: {
          nextToken: decryptedNextToken,
          username,
        },
        responseType: 'json',
      }
    ).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError('Something went wrong');
        }
      }),
    );
  }

  upload(username: string, name: string, text: string, decryptedNextToken: string): Observable<{ nextToken: string }> {
    return this.http.post<{ nextToken: string }>(
      `${this.serverUrl}:${this.port}/files`,
      { username, name, text },
      {
        params: {
          nextToken: decryptedNextToken,
          username,
        },
        responseType: 'json',
      }
    ).pipe(
      catchError(e => {
        if (e.error) {
          return throwError((JSON.parse(e.error) as any).message);
        } else {
          return throwError('Something went wrong');
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
          return throwError('Something went wrong');
        }
      }),
    );
  }
}
