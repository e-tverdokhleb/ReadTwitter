package com.example.hp.readtwitter;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthServiceInterface {
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<OAuthDataContributor> getBearerToketn(@Field("grant_type") String grant_type);
}