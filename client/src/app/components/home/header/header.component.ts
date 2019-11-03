import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AlertController, ToastController } from '@ionic/angular';

import { HttpService } from '../../../services/http.service';
import { File } from '../../../interfaces/file';
import { DataService } from 'src/app/services/data.service';

import { AES, enc, pad } from 'crypto-js';

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
    private dataService: DataService,
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
              console.log(this.dataService.getSessionKey());
              const decryptedText = AES.decrypt(text, this.dataService.getSessionKey(), {
                iv: 'RandomInitVector',
                padding: pad.NoPadding
              });
              console.log(decryptedText);
              console.log(decryptedText.toString());
              console.log(decryptedText.toString(enc.Utf8));
              this.getFile.emit('hui');
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

}
