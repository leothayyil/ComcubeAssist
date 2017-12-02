package com.example.user.comcubeassist.retrofit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 02-12-2017.
 */

public class RetrofitHelper {

    Context context;

    private  void  initRestAdapter(){

        OkHttpClient httpClient= new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();
        Retrofit rest=new Retrofit.Builder()
                .baseUrl("http://shopknekt.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }
}
