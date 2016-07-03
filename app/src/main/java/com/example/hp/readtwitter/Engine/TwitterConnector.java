package com.example.hp.readtwitter.Engine;

import android.os.AsyncTask;

import com.example.hp.readtwitter.Engine.AyncTasks.AuthAsyncTask;
import com.example.hp.readtwitter.Engine.AyncTasks.ReciveDataAsyncTask;
import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.Network.GetUserPostService;
import com.example.hp.readtwitter.Network.OAuthDataContributor;
import com.example.hp.readtwitter.Network.OAuthServiceInterface;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterConnector {
    private static int tweetsCount = 3;
    public static String authorizationHeader = "";

    public void TwitterConnector(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public void run() {
        getConntection();
    }

    private void getConntection() {
        if (authorizationHeader == "") {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
                EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
                return;
            }
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory
                    .create()).baseUrl(Service.BASE_URL).client(Service.httpClientForAuth).build();

            OAuthServiceInterface messages = retrofit.create(OAuthServiceInterface.class);
            Call<OAuthDataContributor> call
                    = messages.getBearerToketn("client_credentials");
            AsyncTask authnetworkCall = new AuthAsyncTask().execute(call);
        } else {
            getMessages();
        }
    }

    public static void getMessages() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.BASE_URL)
                .client(Service.httpClientGetData).addConverterFactory(GsonConverterFactory.create()).build();

        GetUserPostService message = retrofit.create(GetUserPostService.class);
        Call<List<TwitterPost>> call = message.getUserPosts("HromadskeUA", tweetsCount);
        AsyncTask getPosts = new ReciveDataAsyncTask().execute(call);
    }


    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }
}
