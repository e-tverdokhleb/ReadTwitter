package com.example.hp.readtwitter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUserPostService {
    @GET("1.1/statuses/user_timeline.json")
    Call<List<TwitterPosts>> getUserPosts(@Query("screen_name") String screen_name,
                                          @Query("count") int count);
}
