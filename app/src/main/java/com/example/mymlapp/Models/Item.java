package com.example.mymlapp.Models;

import com.google.gson.annotations.Expose;

public class Item {


    @Expose
    private String label;
    @Expose
    private  String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
