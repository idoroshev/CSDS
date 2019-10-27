import { Injectable } from '@angular/core';
import { IRsa, IPublicKey, IPrivateKey } from '../interfaces/rsa';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private rsa: IRsa;
  private sessionKey: string;

  public getRsa(): IRsa {
    return this.rsa;
  }

  public setRsa(rsa: IRsa) {
    this.rsa = rsa;
  }

  public getPublicKey(): IPublicKey {
    return this.rsa.publicKey;
  }

  public setPublicKey(publicKey: IPublicKey) {
    this.rsa.publicKey = publicKey;
  }

  public getPrivateKey(): IPrivateKey {
    return this.rsa.privateKey;
  }

  public setPrivateKey(privateKey: IPrivateKey) {
    this.rsa.privateKey = privateKey;
  }

  public getSessionKey(): string {
    return this.sessionKey;
  }

  public setSessionKey(sessionKey: string) {
    this.sessionKey = sessionKey;
  }
}
