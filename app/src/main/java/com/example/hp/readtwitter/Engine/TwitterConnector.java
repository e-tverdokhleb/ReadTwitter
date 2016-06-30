package com.example.hp.readtwitter.Engine;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.Network.GetUserPostService;
import com.example.hp.readtwitter.Network.OAuthDataContributor;
import com.example.hp.readtwitter.Network.OAuthServiceInterface;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterConnector {
    private int tweetsCount = 5;
    String authorizationHeader = "";

    public void TwitterConnector(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public void run() {
        getConntection();
    }

    public void getConntection() {
        if (authorizationHeader == "") {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
                EventBus.getDefault().post(new MessageEvent(005));
                return;
            }
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String urlApiKey = URLEncoder.encode(UserData.CONSUMER_KEY, "UTF-8");
                            String urlApiSecret = URLEncoder.encode(UserData.CONSUMER_SECRET, "UTF-8");
                            String combined = urlApiKey + ":" + urlApiSecret;
                            String keyBase64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                            Request.Builder ongoing = chain.request().newBuilder()
                                    .addHeader("Authorization", "Basic " + keyBase64Encoded)
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                            Log.d(UserData.TAG, "keyBase64: " + String.valueOf(keyBase64Encoded));
                            return chain.proceed(ongoing.build());
                        }
                    }).build();

            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory
                    .create()).baseUrl(UserData.BASE_URL).client(httpClient).build();

            OAuthServiceInterface messages = retrofit.create(OAuthServiceInterface.class);
            Call<OAuthDataContributor> call
                    = messages.getBearerToketn("client_credentials");
            AsyncTask authnetworkCall = new AuthNetworkCall().execute(call);
        } else {
                getMessages();
        }
    }

    class AuthNetworkCall extends AsyncTask<Call, Void, String> {
        @Override
        protected void onPreExecute() {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())){
                EventBus.getDefault().post(new MessageEvent(005));
                return;
            }
        }

        @Override
        protected String doInBackground(Call... params) {
            try {
                retrofit2.Response response = params[0].execute();
                if (response.code() == 200) {
                    String result = response.body().toString();
                    String token = result.substring(result.indexOf(" ") + 1);
                    authorizationHeader = "Bearer " + token;
                    return authorizationHeader;
                }
                authorizationHeader = "";
                return "001";
            } catch (IOException e) {
                e.printStackTrace();
            }
            authorizationHeader = "";
            return "001";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result == "001") {
                EventBus.getDefault().post(new MessageEvent(001));
                return;
            } else {
                EventBus.getDefault().post(new MessageEvent(002));
                getMessages();
            }
        }

    }


    public void getMessages() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())){
            EventBus.getDefault().post(new MessageEvent(005));
            return;
        }
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder()
                                .header("Authorization", authorizationHeader);
                        return chain.proceed(ongoing.build());
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(UserData.BASE_URL)
                .client(httpClient).addConverterFactory(GsonConverterFactory.create()).build();

        GetUserPostService message = retrofit.create(GetUserPostService.class);
        Call<List<TwitterPost>> call = message.getUserPosts("HromadskeUA", tweetsCount);
        AsyncTask getPosts = new Stream().execute(call);
    }

    class Stream extends AsyncTask<Call, Void, List<TwitterPost>> {
        @Override
        protected void onPreExecute() {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())){
                EventBus.getDefault().post(new MessageEvent(005));
                return;
            }
            Toast.makeText(MainActivity.getMainActivityInstance(), "fetching data...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<TwitterPost> doInBackground(Call... params) {
            try {
                retrofit2.Response<List<TwitterPost>> response = params[0].execute();
                if (response.code() == 200) {
                    return response.body();
                } else return null;
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TwitterPost> result) {
            if (result != null) {
                EventBus.getDefault().post(new MessageEvent(003, result));
            } else {
                EventBus.getDefault().post(new MessageEvent(004));
            }
        }
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }
}
