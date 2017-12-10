package com.github.butul0ve.wtriter;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class TweetsActivity extends AppCompatActivity
        implements SearchFragment.OnFragmentInteractionListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private FloatingActionButton mAddNewTweetFloatActionButton;
    private FloatingActionButton mShowSearchButton;
    private FloatingActionButton mShowMyFeedButton;
    private SearchView mSearchView;
    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_tweet_action_button: {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(getApplicationContext())
                        .session(session)
                        .text("")
                        .darkTheme()
                        .createIntent();
                startActivity(intent);
                break;
            }
            case R.id.show_my_feed_button: {
                loadTweets();
                break;
            }
            case R.id.show_search_button: {
                if (mToolbar.getVisibility() == View.GONE) {
                    mToolbar.setVisibility(View.VISIBLE);
                } else {
                    mToolbar.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            loadTweetsByUserQuery(query);
            mSearchView.clearFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void initUI() {
        initToolbar();
        initButtons();
        mFrameLayout = findViewById(R.id.frame_layout);
        initSearchView();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initButtons() {
        mShowSearchButton = findViewById(R.id.show_search_button);
        mShowSearchButton.setOnClickListener(this);
        mShowMyFeedButton = findViewById(R.id.show_my_feed_button);
        mShowMyFeedButton.setOnClickListener(this);
        mAddNewTweetFloatActionButton = findViewById(R.id.add_new_tweet_action_button);
        mAddNewTweetFloatActionButton.setOnClickListener(this);
    }

    private void initSearchView() {
        mSearchView = findViewById(R.id.search_view);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void loadTweetsByUserQuery(String query) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance(query)).commit();
    }

    private void loadTweets() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new SearchFragment()).commit();
    }
}