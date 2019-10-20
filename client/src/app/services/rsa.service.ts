import { Injectable } from '@angular/core';

import { isPrime, isRelativelyPrime, gcd } from '../utils/index';

@Injectable({
  providedIn: 'root'
})
export class RsaService {

  constructor() { }

  rsa(p: number, q: number) {
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
  }
}
