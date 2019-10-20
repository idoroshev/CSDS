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
