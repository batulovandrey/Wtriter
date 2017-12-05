package com.github.butul0ve.wtriter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.butul0ve.wtriter.R;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * @author Andrey Batulov on 05.12.17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private List<Tweet> mTweets;

    public TweetsAdapter(List<Tweet> mTweets) {
        this.mTweets = mTweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.mTweetTextView.setText(tweet.text);
        holder.mUserNameTextView.setText(tweet.user.name);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}