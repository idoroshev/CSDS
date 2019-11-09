import { Injectable } from '@angular/core';
import { IRsa, IPublicKey, IPrivateKey } from '../interfaces/rsa';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private rsa: IRsa;
  private sessionKey: string;
  private initVector: string;
  private username: string;

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

  public getInitVector(): string {
    return this.initVector;
  }

  public setInitVector(initVector: string) {
    this.initVector = initVector;
  }

  public getSessionKey(): string {
    return this.sessionKey;
  }

  public setSessionKey(sessionKey: string) {
    this.sessionKey = sessionKey;
  }

  public getUsername(): string {
    return this.username;
  }

  public setUsername(username: string) {
    this.username = username;
  }
}
