package com.github.butul0ve.wtriter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.butul0ve.wtriter.R;

/**
 * @author Andrey Batulov on 05.12.17.
 */

public final class TweetViewHolder extends RecyclerView.ViewHolder {

    TextView mTweetTextView;

    public TweetViewHolder(View itemView) {
        super(itemView);
        mTweetTextView = itemView.findViewById(R.id.tweet_text_view);
    }
}