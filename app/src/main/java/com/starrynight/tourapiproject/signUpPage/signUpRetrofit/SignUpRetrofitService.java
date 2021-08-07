package com.starrynight.tourapiproject.signUpPage.signUpRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SignUpRetrofitService {

    @POST("user")
    Call<Void> signUp(@Body UserParams params);

    @GET("user/duplicate/email/{email}")
    Call<Boolean> checkDuplicateEmail(@Path("email")String email);

    @GET("user/duplicate/mobilePhoneNumber/{mobilePhoneNumber}")
    Call<Boolean> checkDuplicateMobilePhoneNumber(@Path("mobilePhoneNumber")String mobilePhoneNumber);

    @POST("myHashTag/{mobilePhoneNumber}")
    Call<Long> createMyHashTag(@Path("mobilePhoneNumber")String mobilePhoneNumber, @Body List<MyHashTagParams> myHashTagParams);

    @GET("user/login/{email}/{password}")
    Call<Long> logIn(@Path("email")String email, @Path("password")String password);

    @GET("user/login/email/{realName}/{mobilePhoneNumber}")
    Call<String> getEmail(@Path("realName")String realName, @Path("mobilePhoneNumber")String mobilePhoneNumber);

    @GET("user/login/password/{email}/{realName}/{mobilePhoneNumber}")
    Call<String> getPassword(@Path("email")String email, @Path("realName")String realName, @Path("mobilePhoneNumber")String mobilePhoneNumber);
}
