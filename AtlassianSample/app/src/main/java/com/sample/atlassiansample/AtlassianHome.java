package com.sample.atlassiansample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sample.atlassiansample.AtlassianParser;
import org.json.JSONObject;

/**
 * The Home activity responsible for taking the message as an input and let user to parse the message
 * to create the JSON structure out of it.
 * This class uses {@link AtlassianParser} to parse the message.
 * This also implements the {@link AtlassianInterface} so to receive the callback from the
 * async call to the {@link AtlassianParser}.
 */
public class AtlassianHome extends Activity {

    /** Private Variables */
    private EditText mChatMessage;
    private Button mParseMessage;
    private TextView mParsedMessage;
    private ProgressBar mProgress;
    private WebView mWebView;
    // Instance of the parser to parse the chat message entered.
    private AtlassianParser mChatMessageParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlassian_home);
        // Initialize the Views.
        initializeViews();
        // Intialize the listeners.
        initializeListeners();
        mChatMessageParse = AtlassianParser.getInstance(AtlassianHome.this,  mCallback);
    }

    /**
     * Initialize the Views.
     */
    private void initializeViews() {
        mChatMessage = (EditText) findViewById(R.id.chatText);
        mParseMessage = (Button) findViewById(R.id.parseMessage);
        mParsedMessage = (TextView) findViewById(R.id.parsedContent);
        // To make the content scrollable in the Textview.
        mParsedMessage.setMovementMethod(new ScrollingMovementMethod());
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    /**
     * Initialize the listeners for the views.
     */
    private void initializeListeners() {
        mParseMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseChatMessage();
            }
        });
    }

    /**
     * API to parse the chat message entered by used. The parsed data will be a json which will comprise
     * following things:
     * 1. mentions - any text prefixed with '@' will be treated as a mention.
     * 2. emoctions - any text inside '(' and ')' will be treated as an emoction.
     * 3. links - any hyperlink in a text will be treated as link. It will be shown with the title of the hyperlink.
     */
    private void parseChatMessage() {
        if(!TextUtils.isEmpty(mChatMessage.getText())) {
            // Parse the chat message. Starting the thread task for this.
            mProgress.setVisibility(View.VISIBLE);
            new Thread(new ParseMessageTask(mChatMessage.getText().toString())).start();
        }
    }

    /**
     * The runnable task responsible for calling the parsing API asynchronously.
     */
    public class ParseMessageTask implements Runnable {
        private String mParseMessageString;
        public ParseMessageTask(String message) {
            mParseMessageString = message;
        }
        @Override
        public void run() {
            mChatMessageParse.parseChatMessage(mParseMessageString);
        }
    }

    /**
     * The implementation of the {@link @AtlassianInterface} to receive the callback after the parsing
     * is done.
     */
    private AtlassianInterface mCallback = new AtlassianInterface() {
        @Override
        public void chatMessageParsed(final String parsedMessage) {
            // Parsed message received
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    try {
                        mParsedMessage.setText(new JSONObject(parsedMessage).toString(2));
                        mProgress.setVisibility(View.GONE);
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }
    };
}
