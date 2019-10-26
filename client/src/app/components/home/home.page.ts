import { Component } from '@angular/core';
import { RsaService } from '../../services/rsa.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  text: string;

  constructor(private rsaService: RsaService) {}

  onGetFile(text: string) {
    this.text = text;
  }

}
