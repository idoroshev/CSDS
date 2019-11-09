import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AlertController, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { utils, padding, ModeOfOperation } from 'aes-js';

import { HttpService } from '../../../services/http.service';
import { File } from '../../../interfaces/file';
import { DataService } from 'src/app/services/data.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
  title = 'BakDor';
  @Output() getFile = new EventEmitter<string>();

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
            this.httpService.getFile(file.name, this.dataService.getUsername()).subscribe(text => {
              const key = utils.utf8.toBytes(this.dataService.getSessionKey());
              const iv = utils.utf8.toBytes(this.dataService.getInitVector());
              const encryptedBytes = utils.hex.toBytes(text);
              const aesCbc = new ModeOfOperation.cbc(key, iv);
              const decryptedBytes = aesCbc.decrypt(encryptedBytes);
              const decryptedText = utils.utf8.fromBytes(padding.pkcs7.strip(decryptedBytes));

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

}