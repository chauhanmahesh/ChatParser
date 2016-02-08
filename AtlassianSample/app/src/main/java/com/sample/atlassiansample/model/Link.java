package com.sample.atlassiansample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model Structure for the JSON of Links. Includes the link title and the actual URL.
 * Created by Chauhan Mahesh on 2/5/2016.
 */
public class Link {

    public Link(String link, String title) {
        mUrl = link;
        mTitle = title;
    }
    @Expose
    @SerializedName("url")
    private String mUrl = null;

    @Expose
    @SerializedName("title")
    private String mTitle = null;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
