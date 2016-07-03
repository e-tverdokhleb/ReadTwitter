package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    private ResponseCode responseCode;
    private String errorMessageCode;

    List<TwitterPost> twitterPosts;

    public List<TwitterPost> getTwitterPosts() {
        return twitterPosts;
    }

    public MessageEvent(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public MessageEvent(ResponseCode responseCode, String errorMessageCode) {
        this.responseCode = responseCode;
        this.errorMessageCode = errorMessageCode;
    }

    public MessageEvent(ResponseCode responseCode, List<TwitterPost> twitterPosts) {
        this.responseCode = responseCode;
        this.twitterPosts = twitterPosts;
    }

    public ResponseCode getResponseCode(){
        return responseCode;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }
}
