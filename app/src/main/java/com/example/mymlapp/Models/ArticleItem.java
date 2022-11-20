package com.example.mymlapp.Models;

import com.google.gson.annotations.Expose;

public class ArticleItem {


    @Expose
    private  String article_text;

    @Expose String article_title;



    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }
}
