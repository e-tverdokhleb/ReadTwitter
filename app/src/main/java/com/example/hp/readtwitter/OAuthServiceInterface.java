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
    @Headers({"Authorization: Basic T2V3cUN4cHljRlV2MFNEMmlhMWRxRldBMTp4U2xNbFVtd3lDZzZKMmlZYVhqSEpHTFZINk81TkcxaWd5dUhmc3JneTQxbU1RYkl1QQ==",
            "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<OAuthDataContributor> getBearerToketn(@Field("grant_type") String grant_type);

}
