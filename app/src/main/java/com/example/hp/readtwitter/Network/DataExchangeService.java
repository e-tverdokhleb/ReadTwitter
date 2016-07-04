package com.example.hp.readtwitter.Network;

import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataExchangeService {
    @GET("1.1/statuses/user_timeline.json")
    Call<List<TwitterPost>> getUserPosts(@Query("screen_name") String screen_name,
                                         @Query("count") int count);

    @GET("1.1/statuses/user_timeline.json")
    Call<List<TwitterPost>> fetchNewPosts(@Query("screen_name") String screen_name,
                                          @Query("since_id") String since_id);

    @GET("1.1/statuses/user_timeline.json")
    Call<List<TwitterPost>> fetchPrevPosts(@Query("screen_name") String screen_name,
                                           @Query("max_id") String lastId,
                                           @Query("count") int count);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<OAuthDataContributor> getBearerToketn(@Field("grant_type") String grant_type);

}
