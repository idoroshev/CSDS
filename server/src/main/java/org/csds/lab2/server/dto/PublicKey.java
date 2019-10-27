package org.csds.lab2.server.dto;

import java.security.Key;

public class PublicKey implements Key {
    private int n;
    private int e;

    public PublicKey() {
    }

    public PublicKey(int n, int e) {
        this.n = n;
        this.e = e;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public String getAlgorithm() {
        return "RSA";
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];
    }
}
