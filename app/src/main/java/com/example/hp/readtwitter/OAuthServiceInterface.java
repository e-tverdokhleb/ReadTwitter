package com.example.hp.readtwitter;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OAuthServiceInterface {
    @POST("oauth2/token")
    Call<ResponseBody> getBearerToketn(@Body GrantType grant_type);

}
