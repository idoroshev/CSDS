package org.csds.lab2.server.dto;

public class PublicKey {
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
}
