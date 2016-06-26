package com.example.hp.readtwitter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.example.hp.readtwitter.Engine.TwitterPostsAdapter;
import com.example.hp.readtwitter.Network.GetUserPostService;
import com.example.hp.readtwitter.Network.OAuthDataContributor;
import com.example.hp.readtwitter.Network.OAuthServiceInterface;
import com.example.hp.readtwitter.TwitterClass.TwitterPost;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    final static String CONSUMER_KEY = "OewqCxpycFUv0SD2ia1dqFWA1";
    final static String CONSUMER_SECRET = "xSlMlUmwyCg6J2iYaXjHJGLVH6O5NG1igyuHfsrgy41mMQbIuA";
    final static String BASE_URL = "https://api.twitter.com/";

    final static String TAG = "MainMenuTAG";

    private static MainActivity instance;

    RecyclerView recyclerView;
    TwitterPostsAdapter twitterPostsAdapter;
    List<TwitterPost> twitterPosts;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        twitterPosts = new ArrayList<TwitterPost>();
        twitterPostsAdapter = new TwitterPostsAdapter(twitterPosts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(twitterPostsAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                getTwitterStream();
            }
        });

        getTwitterStream();
    }


    public void getTwitterStream() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                            String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
                            String combined = urlApiKey + ":" + urlApiSecret;
                            String keyBase64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                            Request.Builder ongoing = chain.request().newBuilder()
                                    .addHeader("Authorization", "Basic " + keyBase64Encoded)
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                            Log.d(TAG, "keyBase64: " + String.valueOf(keyBase64Encoded));
                            return chain.proceed(ongoing.build());
                        }
                    }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .build();

            OAuthServiceInterface messages = retrofit.create(OAuthServiceInterface.class);
            Call<OAuthDataContributor> call
                    = messages.getBearerToketn("client_credentials");

            AsyncTask authnetworkCall = new AuthNetworkCall().execute(call);

        } else {
            Log.v(TAG, "No network connection available");
        }
    }

    private class AuthNetworkCall extends AsyncTask<Call, Void, String> {
        @Override
        protected void onPreExecute() {
            //  ProgressDialog.show(instance.getApplicationContext(), "Loading", "Wait while loading...");
        }

        @Override
        protected String doInBackground(Call... params) {
            try {
                Log.d(TAG, "Server request         : " + (params[0].request().toString()));
                Log.d(TAG, "Server request headers : " + String.valueOf(params[0].request().headers().names()));
                Log.d(TAG, "Server request isHttps : " + (params[0].request().isHttps()));
                Log.d(TAG, "Server request body    : " + String.valueOf(params[0].request().body().toString()));
                retrofit2.Response response = params[0].execute();
                Log.d(TAG, "Server response code: " + String.valueOf(response.code()));
                Log.d(TAG, "Server response: " + String.valueOf(response.headers().toString()));
                if (response.code() == 200) {
                    String result = response.body().toString();
                    String token = result.substring(result.indexOf(" ") + 1);
                    return "Bearer " + token;
                }
                return "error";
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            return "no data has fetched";
        }

        @Override
        protected void onPostExecute(final String result) {
            Log.d(TAG, "AsyncTask result: " + result);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder ongoing = chain.request().newBuilder()
                                    .header("Authorization", result);
                            return chain.proceed(ongoing.build());
                        }
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GetUserPostService message = retrofit.create(GetUserPostService.class);
            Call<List<TwitterPost>> call = message.getUserPosts("HromadskeUA", 10);
            AsyncTask getPosts = new Stream().execute(call);
        }
    }

    private class Stream extends AsyncTask<Call, Void, List<TwitterPost>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<TwitterPost> doInBackground(Call... params) {
            try {
                retrofit2.Response<List<TwitterPost>> response = params[0].execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TwitterPost> result) {
            if (result == null) {
                return;
            }
            twitterPostsAdapter = new TwitterPostsAdapter(result);
            recyclerView.setAdapter(twitterPostsAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}