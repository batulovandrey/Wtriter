package com.github.butul0ve.wtriter;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;

public class TweetsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNewTweetFloatActionButton;
    private SearchView mSearchView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSearchView = findViewById(R.id.search_view);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mAddNewTweetFloatActionButton = findViewById(R.id.add_new_tweet_action_button);
        mAddNewTweetFloatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(getApplicationContext())
                        .session(session)
                        .text("")
                        .darkTheme()
                        .createIntent();
                startActivity(intent);
            }
        });

        loadTweets();
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

    private void loadTweetsByUserQuery(String query) {
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(query)
                .build();
        final TweetTimelineRecyclerViewAdapter adapter = new TweetTimelineRecyclerViewAdapter.Builder(getApplicationContext())
                .setTimeline(searchTimeline)
                .setViewStyle(R.style.tw__TweetDarkWithActionsStyle)
                .build();
        mRecyclerView.setAdapter(adapter);
    }

    private void loadTweets() {
        final StatusesService service = TwitterCore.getInstance().getApiClient().getStatusesService();

        Call<List<Tweet>> listCall = service
                .homeTimeline(10, null, null, null, null, null, null);
        listCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    final FixedTweetTimeline homeTimeline = new FixedTweetTimeline.Builder()
                            .setTweets(result.data)
                            .build();
//                            mTweetsAdapter = new TweetsAdapter(result.data);
                    final TweetTimelineRecyclerViewAdapter adapter = new TweetTimelineRecyclerViewAdapter.Builder(getApplicationContext())
                            .setTimeline(homeTimeline)
                            .setViewStyle(R.style.tw__TweetDarkWithActionsStyle)
                            .build();
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }
}