package com.example.mymlapp;



import com.example.mymlapp.Models.ArticleItem;
import com.example.mymlapp.Models.Item;
import com.example.mymlapp.Models.KeywordItem;
import com.example.mymlapp.Models.SummaryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;



public interface CallManager {
//https://guides.codepath.com/android/consuming-apis-with-retrofit

    /*Form-urlencoded requests are convenient to create the desired request payload without defining a
    class representing the data. You can send selected fields with their values and don’t blow up the request
    body with unnecessary or already known information.*/

    // to use GET methods , read the end of this article / another article to read :https://futurestud.io/tutorials/retrofit-2-add-multiple-query-parameter-with-querymap
    /*Use form-urlencoded requests to send data to a server or API.
     The data is sent within the request body and not as an url parameter.

    Query parameters are used when requesting data from an API or server using specific fields or filter.
    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);# the path is used as seen in link above 'code path'
    */
    @GET("/entitize")// URL STUFF IN RETROFIT :https://futurestud.io/tutorials/retrofit-2-how-to-use-dynamic-urls-for-requests
    Call<List<Item>> get_All_Items();

    @FormUrlEncoded
    @POST ("/entitize")
    Call<List<Item>> send_data_retrieve_enities(@Field("text_sent") String text_sent);

    @FormUrlEncoded
    @POST ("/summarize")
    Call<List<SummaryItem>> send_data_retrieve_summary(@Field("text_sent") String text_sent, @Field("number_of_returned_sentences") int number_of_returned_sentences);

    @FormUrlEncoded
    @POST("/keywords")
    Call<List<KeywordItem>> send_data_retrieve_keywords(@Field("text_sent") String text_sent);

    @FormUrlEncoded
    @POST("/scrap_text")
    Call<List<ArticleItem>> send_data_retrieve_article(@Field("url_sent") String url_sent);


    /*@GET("/get_items_to_map.php")
    Call<List<RegisteredStoresInfo>> getStoresData();

    @FormUrlEncoded
    @POST ("/view_items.php")//get retrofit FROM BASE_URL+ /feeds/flowers.json
    Call<List<ItemsInfo>>getAllProducts(@Field("storeid") int storeid);*/


   /* @FormUrlEncoded
    @POST("/register_info.php")
    Call<ResponseBody> postRegister(
            @Field("storename") String name,
            @Field("storeemail") String email,
            @Field("storepassword") String pass,
            @Field("storecontactme") String contacme,
            @Field("storecategory") String category,
            @Field("storecity") String city,
            @Field("storestreet") String street,
            @Field("storeaccepted") boolean acceptedornot,
            @Field("storex") double x,
            @Field("storey") double y,
            @Field("storeconstruction") String construction
    );

    @FormUrlEncoded
    @POST("/give_access.php")
    Call<ResponseBody> postRegisterUser(
            @Field("email") String email,
            @Field("storename") String storename
    );

    @FormUrlEncoded
    @POST("/save_products.php")
    Call<ResponseBody> postSaveProduct(
            @Field("iteminserteride") int storeid,
            @Field("itemnamee") String itemname,
            @Field("itemdescriptione") String itemdescription,
            @Field("itempricee") double itemprice,
            @Field("itemdiscounte") double itemdiscount,
            @Field("itemsalepricee") double itemsaleprice,
            @Field("itemphotoimage") String itemphotoimage

    );*/








}
