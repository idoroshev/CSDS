import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AlertController, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { utils, padding, ModeOfOperation } from 'aes-js';

import { HttpService } from '../../services/http.service';
import { DataService } from 'src/app/services/data.service';

import { decryptNextToken, encryptNextToken } from '../../utils/index';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  text: string;
  isUpload: boolean;
  fileInput: string;
  filename: string;

  constructor(
    private alertController: AlertController,
    private toastController: ToastController,
    private httpService: HttpService,
    public dataService: DataService,
    private router: Router,
  ) { }

  onGetFile(text: string) {
    this.text = text;
  }

  onChangeUpload(isUpload: boolean) {
    this.isUpload = isUpload;
  }

  uploadFile() {
    const key = utils.utf8.toBytes(this.dataService.getSessionKey());
    const iv = utils.utf8.toBytes(this.dataService.getInitVector());
    const textBytes = utils.utf8.toBytes(this.fileInput);
    const aesCbc = new ModeOfOperation.cbc(key, iv);
    const encryptedBytes = aesCbc.encrypt(padding.pkcs7.pad(textBytes));
    const encryptedHex = utils.hex.fromBytes(encryptedBytes);

    this.httpService.upload(
      this.dataService.getUsername(),
      this.filename,
      encryptedHex,
      this.dataService.getNextToken(),
    ).subscribe(response => {
      this.isUpload = false;
      this.fileInput = '';
      this.filename = '';

      const decryptedNextToken = decryptNextToken(
        this.dataService.getSessionKey(),
        this.dataService.getInitVector(),
        response.nextToken,
      );
      const encryptedNextToken = encryptNextToken(
        this.dataService.getSessionKey(),
        this.dataService.getInitVector(),
        decryptedNextToken + this.dataService.getUsername(),
      );
      this.dataService.setNextToken(encryptedNextToken);
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
