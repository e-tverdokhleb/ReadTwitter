package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    private String message;
    private int eventCode;
    List<TwitterPost> twitterPosts;


    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(List<TwitterPost> twitterPosts) {
        this.twitterPosts = twitterPosts;
    }

    public MessageEvent(int eventCode, List<TwitterPost> twitterPosts) {
        this.eventCode = eventCode;
        this.twitterPosts = twitterPosts;
    }


    public MessageEvent(int eventCode) {
        this.eventCode = eventCode;
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

    public int getEventCode() {
        return this.eventCode;
    }
}
