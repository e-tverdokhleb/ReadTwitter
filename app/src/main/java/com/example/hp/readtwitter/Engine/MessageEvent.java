package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    private String message;
    List<TwitterPost> twitterPosts;


    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(List<TwitterPost> twitterPosts) {
        this.twitterPosts = twitterPosts;
    }

    public String getMessage() {
        return message;
    }

    public void setTwitterPostos(List<TwitterPost> twitterPosts) {
            this.twitterPosts = twitterPosts;
    }

    public List<TwitterPost> getTwitterPosts() {
        return twitterPosts;
    }
}
