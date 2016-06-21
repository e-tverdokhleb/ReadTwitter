package com.example.hp.readtwitter;

import com.google.gson.annotations.SerializedName;

public class OAuthDataContributor {
    String oauth_token;
    String oauth_token_secret;
    boolean oauth_callback_confirmed;

    String id_str;

    @Override
    public String toString(){
        return "hi from authconvereter";
    }
}
