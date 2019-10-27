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
