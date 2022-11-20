package com.example.mymlapp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;


//this is a POJO Class(Plain Old JAVA Object ) that is used to transfer data from Json into this class using retrofit
public class UserCredentials implements Serializable {
    @Expose
    private String storeid;

    @Expose
    private String storeemail;
    //@Expose tells us that json will be converted into (POJO) plain old java object using this class and this variable
    //the name and the other variables(password,storename) must be written exactly the same is in the PHP Code

    @Expose
    private String storepassword;

    @Expose
    private String storename;

    @Expose
    private String storecategory;



    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStoreemail() {
        return storeemail;
    }

    public void setStoreemail(String storeemail) {
        this.storeemail = storeemail;
    }

    public String getStorepassword() {
        return storepassword;
    }

    public void setStorepassword(String storepassword) {
        this.storepassword = storepassword;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStorecategory() {
        return storecategory;
    }

    public void setStorecategory(String storecategory) {
        this.storecategory = storecategory;
    }
}
