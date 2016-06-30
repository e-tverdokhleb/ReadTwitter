package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    public String message;
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

    public List<TwitterPost> getTwitterPosts(){
        return twitterPosts;
    }
}
