package org.csds.lab2.server.dto;

public class File {

    private String text;
    private String nextToken;

    public File() {
    }

    public File(String text, String nextToken) {
        this.text = text;
        this.nextToken = nextToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
