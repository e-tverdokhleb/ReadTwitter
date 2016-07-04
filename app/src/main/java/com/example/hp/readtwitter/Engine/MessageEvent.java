package com.example.hp.readtwitter.Engine;


import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import java.util.List;

public class MessageEvent {
    private ResponseCode responseCode;
    private String errorMessageCode;

    private List<TwitterPost> twitterPosts;
    private List<TwitterPost> prevPosts;
    private List<TwitterPost> newPosts;

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

    public void setPrevPosts(List<TwitterPost> prevPosts) {
        this.prevPosts = prevPosts;
    }

    public List<TwitterPost> getPrevPosts() {
        return prevPosts;
    }

    public void setNewPosts(List<TwitterPost> newPosts) {
        this.newPosts = newPosts;
    }

    public List<TwitterPost> getNewPosts() {
        return newPosts;
    }
}
