package com.example.hp.readtwitter;

import com.google.gson.Gson;

public class Engine {

    public static BearerData parseBearerTokenResponse(String bearerToParse) {
        BearerData bearerData = new BearerData();
        if ((bearerToParse.length() > 0) && (bearerToParse != null)) {
            Gson gson = new Gson();
            bearerData = gson.fromJson(bearerToParse, BearerData.class);
            return bearerData;
        } else {
            return null;
        }
    }
}
