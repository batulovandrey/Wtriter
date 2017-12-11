package com.github.butul0ve.wtriter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.butul0ve.wtriter.R;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {

    TextView mUserNameTextView;
    TextView mTweetTextView;
    TextView mCountLikesTextView;
    ImageView mAvatarImageView;

    public TweetViewHolder(View itemView) {
        super(itemView);
        mUserNameTextView = itemView.findViewById(R.id.user_name_text_view);
        mTweetTextView = itemView.findViewById(R.id.tweet_text_view);
        mCountLikesTextView = itemView.findViewById(R.id.count_likes_text_view);
        mAvatarImageView = itemView.findViewById(R.id.avatar_image_view);
    }
}