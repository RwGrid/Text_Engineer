package com.example.mymlapp.Models;

import com.google.gson.annotations.Expose;

public class SummaryItem {


    @Expose
    private  String text;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}
