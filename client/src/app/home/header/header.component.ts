import { Component, OnInit } from '@angular/core';
import { AlertController } from '@ionic/angular';

import { FileService } from '../../services/file.service';
import { File } from '../../interfaces/file';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  title = 'BakDor';

  constructor(
    private alertController: AlertController,
    private fileService: FileService,
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
            this.fileService.getFile(file.name).subscribe(text => {
              console.log(text);
            });
          }
        }
      ]
    });

    await alert.present();
  }

}
