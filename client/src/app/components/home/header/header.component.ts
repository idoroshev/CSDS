import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AlertController, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { utils, padding, ModeOfOperation } from 'aes-js';

import { HttpService } from '../../../services/http.service';
import { File } from '../../../interfaces/file';
import { DataService } from 'src/app/services/data.service';

import { decryptNextToken, encryptNextToken } from '../../../utils/index';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
  title = 'BakDor';
  isUpload = false;
  @Output() getFile = new EventEmitter<string>();
  @Output() changeUpload = new EventEmitter<boolean>();

  constructor(
    private alertController: AlertController,
    private toastController: ToastController,
    private httpService: HttpService,
    public dataService: DataService,
    private router: Router,
  ) { }

  ngOnInit() {}

  async openFileModal() {
    const alert = await this.alertController.create({
      header: 'Enter file name',
      inputs: [
        {
          name: 'name',
          type: 'text',
          placeholder: 'Enter file name',
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          cssClass: 'secondary',
        }, {
          text: 'Ok',
          handler: (file: File) => {
            this.httpService.getFile(file.name, this.dataService.getUsername(), this.dataService.getNextToken()).subscribe(response => {
              const key = utils.utf8.toBytes(this.dataService.getSessionKey());
              const iv = utils.utf8.toBytes(this.dataService.getInitVector());
              const encryptedBytes = utils.hex.toBytes(response.text);
              const aesCbc = new ModeOfOperation.cbc(key, iv);
              const decryptedBytes = aesCbc.decrypt(encryptedBytes);
              const decryptedText = utils.utf8.fromBytes(padding.pkcs7.strip(decryptedBytes));

              const decryptedNextToken = decryptNextToken(
                this.dataService.getSessionKey(),
                this.dataService.getInitVector(),
                response.nextToken
              );
              const encryptedNextToken = encryptNextToken(
                this.dataService.getSessionKey(),
                this.dataService.getInitVector(),
                decryptedNextToken + this.dataService.getUsername(),
              );
              this.dataService.setNextToken(encryptedNextToken);

              this.getFile.emit(decryptedText);
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
      ]
    });

    await alert.present();
  }

  login() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.httpService.logout().subscribe({
      error: async e => {
        const errorToast = await this.toastController.create({
          message: e,
          duration: 2000,
          color: 'danger',
        });
        await errorToast.present();
      }
    });
  }

  changeIsUpload() {
    this.isUpload = !this.isUpload;
    this.changeUpload.emit(this.isUpload);
  }
}
