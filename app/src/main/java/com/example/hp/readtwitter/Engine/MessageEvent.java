package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    public enum ResponseCode {
        NO_NETWORK_CONNECTION, AUTHORIZATION_ERROR,
        AUTHORIZATION_PASSED, DATA_RECIVED, CANNOT_FETCH_DATA
    }

    private ResponseCode responseCode;

    List<TwitterPost> twitterPosts;

    public List<TwitterPost> getTwitterPosts() {
        return twitterPosts;
    }

    public MessageEvent(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public MessageEvent(ResponseCode responseCode, List<TwitterPost> twitterPosts) {
        this.responseCode = responseCode;
        this.twitterPosts = twitterPosts;
    }

    public ResponseCode getResponseCode(){
        return responseCode;
    }
}
