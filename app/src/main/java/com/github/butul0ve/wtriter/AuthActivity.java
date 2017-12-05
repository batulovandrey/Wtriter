package com.github.butul0ve.wtriter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class AuthActivity extends AppCompatActivity {

    public static final String CONSUMER_KEY = "jnfp0krLC3mqzxlug4Gb8Zq9x";
    public static final String CONSUMER_SECRET = "vKkQMgUEkkO3c3WW6VPDNStjxup8NpZFnYuAAwIdBjpK6M0Id8";

    private TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig config = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new TwitterCore(config));
        setContentView(R.layout.activity_auth);

        twitterLoginButton = findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String userName = result.data.getUserName();
                String hello = "hello, " + userName + "!";
                Toast.makeText(getApplicationContext(), hello, Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), TweetsActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}