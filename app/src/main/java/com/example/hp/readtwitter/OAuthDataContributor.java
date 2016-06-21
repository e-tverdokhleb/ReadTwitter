package com.example.hp.readtwitter;

import com.google.gson.annotations.SerializedName;

public class OAuthDataContributor {
    @SerializedName("token_type")
    String token_type;

    @SerializedName("access_token")
    String access_token;


    @Override
    public String toString(){
        return token_type+ ": " +access_token;
    }
}
