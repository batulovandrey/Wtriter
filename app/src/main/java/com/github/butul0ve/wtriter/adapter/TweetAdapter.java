package com.github.butul0ve.wtriter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.butul0ve.wtriter.R;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private List<Tweet> mTweets;

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.mUserNameTextView.setText(tweet.user.name);
        holder.mTweetTextView.setText(tweet.text);
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 0 : mTweets.size();
    }

    public void setTweets(List<Tweet> tweets) {
        mTweets = tweets;
        notifyDataSetChanged();
    }
}