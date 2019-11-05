import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { HttpService } from '../services/http.service';
import { DataService } from '../services/data.service';
import { AlertController, ToastController } from '@ionic/angular';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private alertController: AlertController,
    private toastController: ToastController,
    private router: Router,
    private httpService: HttpService,
    private dataService: DataService,
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({
      withCredentials: true,
    });

    return next.handle(req).pipe(
      catchError(err => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            this.router.navigate(['/login']);
          } else if (err.status === 419) {
            this.httpService.logout().subscribe(() => {
              this.dataService.setInitVector('');
              this.dataService.setUsername('');
              this.dataService.setSessionKey('');
              this.dataService.setRsa({ privateKey: null, publicKey: null });
              this.router.navigate(['/login']);
            },
            async e => {
              const errorToast = await this.toastController.create({
                message: e,
                duration: 2000,
                color: 'danger',
              });
              await errorToast.present();
            });
          }
        }

        return throwError(err);
      }),
    );
  }
}
