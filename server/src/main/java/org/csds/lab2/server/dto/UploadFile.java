package org.csds.lab2.server.dto;

public class UploadFile {

    private String name;
    private String text;

    public UploadFile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
