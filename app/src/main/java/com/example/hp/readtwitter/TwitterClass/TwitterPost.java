package com.example.hp.readtwitter.TwitterClass;

import java.util.ArrayList;
import java.util.List;

public class TwitterPost {
    private static int lsstId = 0;
    private String created_at;
    private String id;
    private String id_str;
    private String text;
    private String source;
    private Entities entities;


    @Override
    public String toString() {
        return created_at + ": " + text;
    }

    public String getDate() {
        return created_at;
    }

    public String getText() {
        if ((text != "") || (text != null)) {
            return text.substring(0, text.indexOf("http") - 1);
        } else return "";
    }

    public String getMediaUrl() {
        if ((entities.media != null) && (entities.media.size() >= 1)) {
            if ((entities.media.get(0).getMediaUrl() != "")
                    && (entities.media.get(0).getMediaUrl() != null)) {
                return String.valueOf(entities.media.get(0).getMediaUrl());
            } else return "no media Url";
        } else return "no media Url";
    }

    public String getUrl() {
        if ((entities.urls != null) && (entities.urls.size() >= 1)) {
            if ((entities.urls.get(0).getUrl() != "")
                    && (entities.urls.get(0).getUrl() != null)) {
                return String.valueOf(entities.urls.get(0).getUrl());
            } else return "no Url";
        } else return "no Url";
    }

}

class Entities {
    List<Media> media = new ArrayList();
    List<Urls> urls = new ArrayList();

    class Media {
        private String media_url;

        public String getMediaUrl() {
            return media_url;
        }
    }

    class Urls {
        private String url;
        private String expanded_url;
        private String display_url;

        public String getUrl() {
            return url;
        }
    }
}

