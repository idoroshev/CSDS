export interface IPublicKey {
  e: number;
  n: number;
}

export interface IPrivateKey {
  d: number;
  n: number;
}

export interface IRsa {
  publicKey: IPublicKey;
  privateKey: IPrivateKey;
}
