package com.example.mymlapp.Controller;


public class Constants {
    //static variable will get the memory only once, if any object changes the value of the static variable,
    // it will retain its value.
    //without the use of static , the variables will change every time u create an object out of them

    //final keyword does the following :stop value change,stop method overriding ,stop inheritance .
    public  static final class Http{
        public  static final String BASE_URL="https://giftstorestour.000webhostapp.com";

    }
    public  static final class Databases{
        //If you make any variable as final, you cannot change the value of final variable(It will be constant).

    }

    public static final class REFERENCE {
        //public  static final String PRODUCT= Config.PACKAGE_NAME + "flower";
    }
    public static final class Config {
        //public  static final String PACKAGE_NAME="";
    }
}
