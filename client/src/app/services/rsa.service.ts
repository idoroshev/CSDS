import { Injectable } from '@angular/core';

import { isPrime, isRelativelyPrime, pow } from '../utils/index';
import { IRsa, IPrivateKey } from '../interfaces/rsa';

@Injectable({
  providedIn: 'root'
})
export class RsaService {
  rsa(p: number, q: number): IRsa {
    if (!isPrime(p) || !isPrime(q)) {
      return null;
    }

    const n = p * q;
    const fi = (p - 1) * (q - 1);

    let e;
    for (let i = 2; i < fi; ++i) {
      if (isPrime(i) && isRelativelyPrime(i, fi)) {
        e = i;
        break;
      }
    }

    let d;
    for (let i = 0; i <= 9; ++i) {
      const x = 1 + (i * fi);
      if (x % e === 0) {
        d = x / e;
        break;
      }
    }

    return {
      publicKey: { e, n },
      privateKey: { d, n }
    } as IRsa;
  }

  decryptKey(sessionKey: string, privateKey: IPrivateKey) {
    const chars: string[] = sessionKey.split(' ');
    let result = '';
    chars.forEach(char => {
      const charCode = pow(Number(char), privateKey.d, privateKey.n);
      result += String.fromCharCode(charCode);
    });
    return result;
  }
}
