import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AlertController, ToastController } from '@ionic/angular';

import { HttpService } from '../../../services/http.service';
import { File } from '../../../interfaces/file';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  title = 'BakDor';
  @Output() getFile = new EventEmitter<string>();

  constructor(
    private alertController: AlertController,
    private toastController: ToastController,
    private httpService: HttpService,
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
            this.httpService.getFile(file.name).subscribe(text => {
              this.getFile.emit(text);
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
