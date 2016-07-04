package com.example.hp.readtwitter.Engine;

import android.os.AsyncTask;

import com.example.hp.readtwitter.Engine.AyncTasks.AuthAsyncTask;
import com.example.hp.readtwitter.Engine.AyncTasks.FetchNewPostsAsyncTask;
import com.example.hp.readtwitter.Engine.AyncTasks.FetchPrevPostsAsyncTask;
import com.example.hp.readtwitter.Engine.AyncTasks.ReciveDataAsyncTask;
import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.Network.DataExchangeService;
import com.example.hp.readtwitter.Network.OAuthDataContributor;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterConnector {
    private static int tweetsCount = 5;
    public static String authorizationHeader = "";

    public void TwitterConnector(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public void loadPosts() {
        if (MainActivity.twitterPostsAdapter.getItemCount() > 0) {
            fetchNewPosts();
        } else {
            firstRun();
        }
    }

    private void firstRun() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
        if (authorizationHeader == "") {
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory
                    .create()).baseUrl(Service.BASE_URL).client(Service.httpClientForAuth).build();
            DataExchangeService messages = retrofit.create(DataExchangeService.class);
            Call<OAuthDataContributor> call
                    = messages.getBearerToketn("client_credentials");
            AsyncTask authnetworkCall = new AuthAsyncTask().execute(call);
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.BASE_URL)
                    .client(Service.httpClientGetData).addConverterFactory(GsonConverterFactory.create()).build();

            DataExchangeService getUserPostService = retrofit.create(DataExchangeService.class);

            Call<List<TwitterPost>> call = getUserPostService.getUserPosts(Service.defaultUserScreenName, tweetsCount);
            if (!Service.isFetchNewPostsAsyncTaskExecute) {
                AsyncTask getPosts = new ReciveDataAsyncTask().execute(call);
            }
        }
    }

    public void fetchNewPosts() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
        if (authorizationHeader.isEmpty()) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_ERROR, "need authorization"));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.BASE_URL)
                .client(Service.httpClientGetData).addConverterFactory(GsonConverterFactory.create()).build();

        DataExchangeService getUserPostService = retrofit.create(DataExchangeService.class);
        Call<List<TwitterPost>> call
                = getUserPostService.fetchNewPosts(Service.defaultUserScreenName, MainActivity.twitterPostsAdapter.getTweetId(0));
        if (!Service.isFetchNewPostsAsyncTaskExecute) {
            AsyncTask getPosts = new FetchNewPostsAsyncTask().execute(call);
        }
    }

    public static void fetchPreviouslyPosts() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
        if (authorizationHeader.isEmpty()) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_ERROR, "need authorization"));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.BASE_URL)
                .client(Service.httpClientGetData).addConverterFactory(GsonConverterFactory.create()).build();

        DataExchangeService getUserPostService = retrofit.create(DataExchangeService.class);
        Call<List<TwitterPost>> call = getUserPostService.fetchPrevPosts(Service.defaultUserScreenName, MainActivity.twitterPostsAdapter.getLastTweetId(), 2);

        if (!Service.isFetchPrevPostsAsyncTaskExecute) {
            AsyncTask getPosts = new FetchPrevPostsAsyncTask().execute(call);
        }

    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }
}
