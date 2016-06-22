package com.example.hp.readtwitter;

public class BearerData {
    String token_type;
    String access_token;

    @Override
    public String toString() {
        return token_type + " " + access_token;
    }
}
