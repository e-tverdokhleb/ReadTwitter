package com.example.hp.readtwitter;

import com.google.gson.annotations.SerializedName;

public class GrantType {

    @SerializedName("grant_type")
    private String grant_type;

    public GrantType(String grantType) {
        this.grant_type = grantType;
    }
}
