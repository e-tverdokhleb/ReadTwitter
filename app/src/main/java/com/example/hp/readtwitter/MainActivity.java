package com.example.hp.readtwitter;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
*/

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * Demonstrates how to use a twitter application keys to access a user's timeline
 */
public class MainActivity extends ListActivity {
    final static String CONSUMER_KEY = "OewqCxpycFUv0SD2ia1dqFWA1";
    final static String CONSUMER_SECRET = "xSlMlUmwyCg6J2iYaXjHJGLVH6O5NG1igyuHfsrgy41mMQbIuA";
    final static String BASE_URL = "https://api.twitter.com/";
    final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";


    private ListActivity activity;
    final static String ScreenName = "therockncoder";
    final static String TAG = "MainMenuTAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

            getAccess();

    }

    public void getAccess() {
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
                                    .addHeader("Authorization", "Basic " +keyBase64Encoded)
                                    .addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

                            return chain.proceed(ongoing.build());
                        }
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .build();


            OAuthServiceInterface messages = retrofit.create(OAuthServiceInterface.class);
            GrantType grantType = new GrantType("client_credentials");
            Call<ResponseBody> call
                    = messages.getBearerToketn(grantType);

            AsyncTask authnetworkCall = new AuthNetworkCall().execute(call);

        } else {
            Log.v(TAG, "No network connection available.");
        }
    }

    private class AuthNetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Call... params) {
            try {
                Log.d(TAG, "Server request         : " + (params[0].request().toString()));
                Log.d(TAG, "Server request headers : " + (params[0].request().headers().size()));
                Log.d(TAG, "Server request isHttps : " + (params[0].request().isHttps()));
                Log.d(TAG, "Server request body    : " + String.valueOf(params[0].request().body().toString()));
                retrofit2.Response response = params[0].execute();
                Log.d(TAG, "Server response: " + String.valueOf(response.code()));
                Log.d(TAG, "Server response: " + String.valueOf(response.headers().toString()));
                Log.d(TAG, "Server response: " + String.valueOf(response.errorBody().string()));
                return String.valueOf(response.body());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            return "no data has fetched";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

/*
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0]);
            }
            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            Twitter twits = jsonToTwitter(result);

            // lets write the results to the console as well
            for (Tweet tweet : twits) {
                Log.i(LOG_TAG, tweet.getText());
            }

            // send the tweets to the adapter for rendering
            ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(activity, android.R.layout.simple_list_item_1, twits);
            setListAdapter(adapter);
        }

        // converts a string of JSON data into a Twitter object
        private Twitter jsonToTwitter(String result) {
            Twitter twits = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    twits = gson.fromJson(result, Twitter.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return twits;
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getTwitterStream(String screenName) {
            String results = null;

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);
                com.example.TwitterTutorial.Authenticated auth = jsonToAuthenticated(rawAuthorization);

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.token_type.equals("bearer")) {

                    // Step 3: Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                    httpGet.setHeader("Content-Type", "application/json");
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }
    }
    */
}
