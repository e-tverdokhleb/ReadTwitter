package com.example.hp.readtwitter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 21.06.2016.
 */
public class GrantType {
    @SerializedName("grant_type")
        private String grant_type;

        public GrantType(String grantType) {
            this.grant_type = grantType;
        }
}
