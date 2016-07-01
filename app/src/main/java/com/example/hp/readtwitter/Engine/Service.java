package com.example.hp.readtwitter.Engine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;


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


}
