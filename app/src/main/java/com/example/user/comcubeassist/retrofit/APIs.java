package com.example.user.comcubeassist.retrofit;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by USER on 02-12-2017.
 */

public interface APIs {

    @GET("web-api/user_login.php?")
    Call<JsonElement> login(@Query("user_name") String user_name, @Query("password")
            String password, @Query("pos_lat") String pos_lat, @Query("pos_long") String pos_long);


    @POST("/web-api/insert_shop.php")
    Call<JsonElement>feedback(@Body String shopName, @Body int user_id, @Body String pos_lat, @Body String pos_lon, @Body String feedback);

}
