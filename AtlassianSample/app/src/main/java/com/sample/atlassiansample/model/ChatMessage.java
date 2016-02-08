package com.sample.atlassiansample.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model Structure for the JSON which will be prepared for the ChatMessage.
 * Lists all the mentions, emoticons and links.
 * Created by Chauhan Mahesh on 2/5/2016.
 */
public class ChatMessage {
    @Expose
    @SerializedName("mentions")
    private List<String> mMentions = null;
    @Expose
    @SerializedName("emoticons")
    private List<String> mEmoticons = null;
    @Expose
    @SerializedName("links")
    private List<Link> mLinks = null;

    public List<String> getMentions() {
        return mMentions;
    }

    public void setMentions(List<String> mentions) {
        mMentions = mentions;
    }

    public List<String> getEmoticons() {
        return mEmoticons;
    }

    public void setEmoticons(List<String> emoticons) {
        mEmoticons = emoticons;
    }

    public List<Link> getLinks() {
        return mLinks;
    }

    public void setLinks(List<Link> links) {
        mLinks = links;
    }

}
