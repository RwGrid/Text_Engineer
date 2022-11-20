package com.example.mymlapp.Controller;


import com.example.mymlapp.CallManager;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



//Retrofit:
    /*
    This library makes downloading JSON or XML data from a web API fairly straightforward. Once the data is downloaded then
    it is parsed into a Plain Old Java Object (POJO) which must be defined for each "resource" in the response.
     */

public class RestManager {

    // the purpose of this class is to create a Retrofit api using any and i insist on any interface having any file
    //proof: here we only used the BASE_URL , and in the interface to add the rest of the url
    // so if we have a different file we can create another interface

    private CallManager CallOwnerInfo;

    public CallManager getAllCallsService() {

        if (CallOwnerInfo == null) {//here we are building a Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.134:5000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CallOwnerInfo = retrofit.create(CallManager.class);

        }
        //.addConverterFactory(GsonConverterFactory.create()) is used to
        // Retrofit relied on the Gson library to serialize and deserialize JSON data.
        //Serialization is a mechanism of converting the state of an object into a byte stream.
        // Deserialization is the reverse process where the byte stream is used to recreate the actual Java object in memory.\
        // This mechanism is used to persist the object.


        return CallOwnerInfo;
    }
}
