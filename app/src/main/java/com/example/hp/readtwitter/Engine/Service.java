package com.example.hp.readtwitter.Engine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Service {
    public static String getTimeAgo(String time) {
        if ((time != null) && (time.length() > 1)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss ZZZ yyyy");
            try {
                return DateUtils.getRelativeTimeSpanString(simpleDateFormat.parse(time).getTime()).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "some time ago...";
    }

    public static boolean isConnection(AppCompatActivity appCompatActivity) {
        ConnectivityManager connMgr
                = (ConnectivityManager) appCompatActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())) {
            return true;
        } else {
            Log.v(UserData.TAG, "No network connection available");
            return false;
        }
    }

    public static OkHttpClient httpClientForAuth = new OkHttpClient.Builder()
            .addInterceptor(new okhttp3.Interceptor() {
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

    public static OkHttpClient httpClientGetData = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder ongoing = chain.request().newBuilder()
                            .header("Authorization", TwitterConnector.authorizationHeader)
                            .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                    return chain.proceed(ongoing.build());
                }
            }).build();
}
