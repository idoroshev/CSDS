import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FileService {
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
        const errorMessage = JSON.parse(e.error).message;
        return throwError(errorMessage);
      }),
    );
  }
}
