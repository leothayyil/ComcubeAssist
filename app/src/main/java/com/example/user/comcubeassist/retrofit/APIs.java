package com.example.user.comcubeassist.retrofit;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by USER on 02-12-2017.
 */

public interface APIs {
    @GET("/user_login.php?")
    Call<JsonElement> login(String user_name,String password,String pos_lat,String pos_long);
}
