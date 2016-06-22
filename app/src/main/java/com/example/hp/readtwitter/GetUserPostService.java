package com.example.hp.readtwitter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetUserPostService {
    @Headers({"Authorization: Bearer AAAAAAAAAAAAAAAAAAAAACw6vQAAAAAAH7BFEnsb2tVZKY%2F%2BQ9LPCdskKw8%3Drw5ONV9dC0bCdX0qgkhhYTNdKuN0DyZYkPk6HJppQ98Tv54CQE"})
    @GET("1.1/statuses/user_timeline.json") //?count=100
    Call<List<TwitterPosts>> getUserPosts(@Query("screen_name") String screen_name);
}
