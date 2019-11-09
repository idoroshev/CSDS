import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastController } from '@ionic/angular';

import { HttpService, ISessionResponse } from 'src/app/services/http.service';
import { RsaService } from 'src/app/services/rsa.service';
import { DataService } from 'src/app/services/data.service';

import { Router } from '@angular/router';
import { utils, padding, ModeOfOperation } from 'aes-js';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginData = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(
    private httpService: HttpService,
    private router: Router,
    private toastController: ToastController,
    private rsaService: RsaService,
    private dataService: DataService,
  ) { }

  onSubmit() {
    const rsa = this.rsaService.rsa(17, 31);
    this.dataService.setRsa(rsa);

    const username: string = this.loginData.get('username').value;
    const password: string = this.loginData.get('password').value;

    this.httpService.session(
      username,
      { ...rsa.publicKey }
    ).subscribe(
      (sessionResponse: ISessionResponse) => {
        const sessionKey = this.rsaService.decryptKey(sessionResponse.sessionKey, this.dataService.getPrivateKey());
        const initVector = this.rsaService.decryptKey(sessionResponse.initVector, this.dataService.getPrivateKey());
        this.dataService.setSessionKey(sessionKey);
        this.dataService.setInitVector(initVector);
        this.dataService.setUsername(username);

        const key = utils.utf8.toBytes(sessionKey);
        const iv = utils.utf8.toBytes(initVector);
        const textBytes = utils.utf8.toBytes(password);
        const aesCbc = new ModeOfOperation.cbc(key, iv);
        const encryptedBytes = aesCbc.encrypt(padding.pkcs7.pad(textBytes));
        const encryptedHex = utils.hex.fromBytes(encryptedBytes);

        this.httpService.login(username, encryptedHex).subscribe(
          () => {
            this.router.navigate(['/home']);
          },
          async e => {
            const errorToast = await this.toastController.create({
              message: e,
              duration: 2000,
              color: 'danger',
            });
            await errorToast.present();
          }
        );
      },
      async e => {
        const errorToast = await this.toastController.create({
          message: e,
          duration: 2000,
          color: 'danger',
        });
        await errorToast.present();
      }
    );
  }
}
