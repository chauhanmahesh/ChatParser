package com.sample.atlassiansample;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.reflect.TypeToken;
import com.sample.atlassiansample.model.ChatMessage;
import com.sample.atlassiansample.model.Link;
import com.sample.atlassiansample.util.JsonUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chauhan Mahesh on 2/5/2016.
 *
 * Singleton class responsible for parsing the chat message passed to it.
 */
public class AtlassianParser {

    private static AtlassianParser sInstance;
    // Pattern to match all the mentions in the strings.
    private final Pattern mMentionPattern = Pattern.compile("(@[a-zA-Z0-9_][^\\s]+)");
    // Pattern to match all the weblinks in the string.
    private final Pattern mLinkPattern = Pattern.compile("(https?:\\/\\/[^\\s]+)");
    // Pattern to match all the emoticon in the string.
    private final Pattern mEmoticonPattern = Pattern.compile("\\(([^)]+)\\)");
    private Activity mContext;
    private AtlassianInterface mCallback;
    private ChatMessage mParsedChatMessage;

    private static int sLinksCounter = 0;

    private AtlassianParser() {

    }

    private AtlassianParser(Activity context, AtlassianInterface callback) {
        mContext = context;
        mCallback = callback;
    }

    public static AtlassianParser getInstance(Activity context, AtlassianInterface callback) {
        if(sInstance == null) {
            sInstance = new AtlassianParser(context, callback);
        }
        return sInstance;
    }

    /**
     * API responsible for actual parsing of the chat message received.
     * 1. Finds all the mentions in the message using the RegEx.
     * 2. Finds all the emoticons in the message using the RegEx
     * 3. Finds all the links in the message using the RegEx.
     * 4. Finds the title of all the links using the JSOUP HTML Parser. It actually parses the
     * HTML content of the page and finds the title out of it. It works only if there is an
     * internet connection available. Else title will not be returned.
     * @param chatMessage
     */
    public void parseChatMessage(String chatMessage) {
        mParsedChatMessage = new ChatMessage();
        // Find all the mentions first.
        final Matcher mentionMatcher = mMentionPattern.matcher(chatMessage);
        List<String> mentions = null;
        while(mentionMatcher.find()) {
            if(mentions == null) {
                mentions = new ArrayList<>();
            }
            mentions.add(mentionMatcher.group(1).substring(1));
        }
        mParsedChatMessage.setMentions(mentions);

        // Find all the emoticons.
        final Matcher emoticonMatcher = mEmoticonPattern.matcher(chatMessage);
        List<String> emoticons = null;
        while(emoticonMatcher.find()) {
            if(emoticons == null) {
                emoticons = new ArrayList<>();
            }
            String match = emoticonMatcher.group(1);
            // Let's take only the emoticons which are of length 15 or less.
            if(!TextUtils.isEmpty(match) && match.length() <=15) {
                emoticons.add(match);
            }
        }
        mParsedChatMessage.setEmoticons(emoticons);

        // Find all the links. This process will take time as we need to fetch the Web Page title as well.
        // We are trying to get the page title by loading the url in to the website and then will
        // try to retrieve the title once the page is loaded.
        final Matcher linksMatcher = mLinkPattern.matcher(chatMessage);
        List<Link> links = null;
        while(linksMatcher.find()) {
            if(links == null) {
                links = new ArrayList<>();
            }
            String link = linksMatcher.group(1);
            try {
                // Just check the internet connection here, otherwise only the url
                // will be added.
                String title = null;
                if(isNetworkAvailable()) {
                    Document doc = Jsoup.connect(link).get();
                    title = doc.title();
                }
                // As title is not find yet, lets set null.
                links.add((new Link(link, title)));
            } catch(IOException e) {

            }
        }

        mParsedChatMessage.setLinks(links);
        mCallback.chatMessageParsed(JsonUtil.convertTypeToJson(mContext, mParsedChatMessage, new TypeToken<ChatMessage>() {
        }.getType()));
    }

    /**
     * Chesk if there is internet connection available or not.
     * @return
     */
    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
