import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastController } from '@ionic/angular';

import { HttpService } from 'src/app/services/http.service';
import { RsaService } from 'src/app/services/rsa.service';
import { DataService } from 'src/app/services/data.service';

import { AES, enc, pad, Padding } from 'crypto-js';
import { Router } from '@angular/router';
import { padding } from 'aes-js';

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
      EncryptedSessionKey => {
        const sessionKey = this.rsaService.decryptSessionKey(EncryptedSessionKey, this.dataService.getPrivateKey());
        this.dataService.setSessionKey(sessionKey);
        this.dataService.setUsername(username);
        const encryptedPassword = AES.encrypt(password, sessionKey, {
          iv: 'RandomInitVector',
          padding: pad.NoPadding
        });
        const decryptedPassword = AES.decrypt(encryptedPassword, sessionKey, {
          iv: 'RandomInitVector',
          padding: pad.NoPadding
        });

        //console.log(this.dataService.getSessionKey());
        //console.log(encryptedPassword.toString().length);
        //console.log(decryptedPassword.toString(enc.Utf8));

        this.httpService.login(username, encryptedPassword.toString()).subscribe(
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
