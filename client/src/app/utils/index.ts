import { utils, padding, ModeOfOperation } from 'aes-js';

export function isPrime(num: number) {
  const s = Math.sqrt(num);
  for (let i = 2; i <= s; ++i) {
    if (num % i === 0) {
      return false;
    }
  }
  return num > 1;
}

export function gcd(a: number, b: number) {
  let t;
  while (b !== 0) {
    t = a;
    a = b;
    b = t % b;
  }
  return a;
}

export function isRelativelyPrime(a: number, b: number) {
  return gcd(a, b) === 1;
}

export function pow(a: number, b: number, mod: number): number {
  let result = 1;
  while (b > 0) {
    if (b % 2 === 0) {
      a = (a * a) % mod;
      b = b / 2;
    } else {
      result = (result * a) % mod;
      b -= 1;
    }
  }

  return result;
}

export function encryptNextToken(sessionKey: string, initVector: string, nextToken: string): string {
  const key = utils.utf8.toBytes(sessionKey);
  const iv = utils.utf8.toBytes(initVector);
  const textBytes = utils.utf8.toBytes(nextToken);
  const aesCbc = new ModeOfOperation.cbc(key, iv);
  const encryptedBytes = aesCbc.encrypt(padding.pkcs7.pad(textBytes));
  const encryptedHex = utils.hex.fromBytes(encryptedBytes);
  return encryptedHex;
}

export function decryptNextToken(sessionKey: string, initVector: string, nextToken: string): string {
  const key = utils.utf8.toBytes(sessionKey);
  const iv = utils.utf8.toBytes(initVector);
  const encryptedBytes = utils.hex.toBytes(nextToken);
  const aesCbc = new ModeOfOperation.cbc(key, iv);
  const decryptedBytes = aesCbc.decrypt(encryptedBytes);
  const decryptedNextToken = utils.utf8.fromBytes(padding.pkcs7.strip(decryptedBytes));
  return decryptedNextToken;
}
