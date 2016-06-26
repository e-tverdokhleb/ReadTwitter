package com.example.hp.readtwitter.Engine;

import android.text.format.DateUtils;
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
        } else {

        }
        return "";
    }
}
