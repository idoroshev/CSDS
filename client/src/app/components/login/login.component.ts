import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastController } from '@ionic/angular';
import { Router } from '@angular/router';

import { HttpService, ISessionResponse } from 'src/app/services/http.service';
import { RsaService } from 'src/app/services/rsa.service';
import { DataService } from 'src/app/services/data.service';

import { utils, padding, ModeOfOperation } from 'aes-js';
import { decryptNextToken, encryptNextToken } from '../../utils/index';

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
      (response: ISessionResponse) => {
        const sessionKey = this.rsaService.decryptKey(response.sessionKey, this.dataService.getPrivateKey());
        const initVector = this.rsaService.decryptKey(response.initVector, this.dataService.getPrivateKey());
        this.dataService.setSessionKey(sessionKey);
        this.dataService.setInitVector(initVector);
        this.dataService.setUsername(username);

        const key = utils.utf8.toBytes(sessionKey);
        const iv = utils.utf8.toBytes(initVector);
        const textBytes = utils.utf8.toBytes(password);
        const aesCbc = new ModeOfOperation.cbc(key, iv);
        const encryptedBytes = aesCbc.encrypt(padding.pkcs7.pad(textBytes));
        const encryptedHex = utils.hex.fromBytes(encryptedBytes);

        let decryptedNextToken = decryptNextToken(sessionKey, initVector, response.nextToken);
        let encryptedNextToken = encryptNextToken(sessionKey, initVector, decryptedNextToken + username);
        this.dataService.setNextToken(encryptedNextToken);

        this.httpService.login(username, encryptedHex, encryptedNextToken).subscribe(
          ({ nextToken }) => {
            console.log(nextToken);
            decryptedNextToken = decryptNextToken(sessionKey, initVector, nextToken);
            encryptedNextToken = encryptNextToken(sessionKey, initVector, decryptedNextToken + username);
            this.dataService.setNextToken(encryptedNextToken);
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
