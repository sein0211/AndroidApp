package com.starrynight.tourapiproject.postWritePage.postWriteRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {



    private static final String BASE_URL = "http://192.168.35.75:8080/v1/";




    public static PostHashTagRetrofitService getApiService(){return getInstance().create(PostHashTagRetrofitService.class);}

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
