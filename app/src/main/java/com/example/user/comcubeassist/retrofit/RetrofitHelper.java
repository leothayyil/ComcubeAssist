package com.example.user.comcubeassist.retrofit;

import android.content.Context;

import com.example.user.comcubeassist.LoginActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 02-12-2017.
 */

public class RetrofitHelper {

    Context context;
    static APIs apIs;


    public RetrofitHelper(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static APIs getApIs() {
        return apIs;
    }

    public static void setApIs(APIs apIs) {
        RetrofitHelper.apIs = apIs;
    }

    public RetrofitHelper(LoginActivity loginActivity) {
        initRestAdapter();
    }

    private  void  initRestAdapter(){

        OkHttpClient httpClient= new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();
        Retrofit rest=new Retrofit.Builder()
                .baseUrl("http://comcubecochin.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        setApIs(rest.create(APIs.class));
    }

}
