import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastController } from '@ionic/angular';

import { HttpService } from 'src/app/services/http.service';
import { RsaService } from 'src/app/services/rsa.service';
import { pow } from 'src/app/utils';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginData = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(
    private httpService: HttpService,
    private toastController: ToastController,
    private rsaService: RsaService,
    private dataService: DataService,
  ) { }

  onSubmit() {
    const rsa = this.rsaService.rsa(17, 31);
    this.dataService.setRsa(rsa);

    this.httpService.login(
      this.loginData.get('username').value,
      this.loginData.get('password').value,
      { ...rsa.publicKey },
    ).subscribe(
      EncryptedSessionKey => {
        const sessionKey = this.rsaService.decryptSessionKey(EncryptedSessionKey, this.dataService.getPrivateKey());
        this.dataService.setSessionKey(sessionKey);
        console.log(this.dataService.getSessionKey());
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
