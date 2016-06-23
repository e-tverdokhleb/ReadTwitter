package com.example.hp.readtwitter;

/**
 * Created by HP on 22.06.2016.
 */
public class TwitterPost {
    private static int lsstId = 0;
    private String created_at;
    private String text;
    private String description;
    private String url;


    @Override
    public String toString() {
        return text;
    }

    public String getDate() {
        return created_at;
    }

    public String getTitle() {
        return text;
    }

    public String getDescription() {
        return description;
    }



    public String getUrl() {
        return url;
    }

}
