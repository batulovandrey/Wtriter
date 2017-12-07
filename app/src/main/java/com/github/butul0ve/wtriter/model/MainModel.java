package com.github.butul0ve.wtriter.model;

import com.github.butul0ve.wtriter.adapter.TweetAdapter;
import com.github.butul0ve.wtriter.presenter.MainPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public class MainModel {

    private MainPresenter mMainPresenter;
    private TweetAdapter mAdapter;

    public MainModel(MainPresenter mMainPresenter) {
        this.mMainPresenter = mMainPresenter;
    }

    public void getSearchAdapter(String query) {
        final SearchService service = TwitterCore.getInstance().getApiClient().getSearchService();
        Call<Search> call = service.tweets(query, null, null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                if (result != null) {
                    mAdapter = new TweetAdapter(result.data.tweets);
                    mAdapter.notifyDataSetChanged();
                    mMainPresenter.updateData(mAdapter);
                } else {
                    mMainPresenter.showToast("error");
                }
            }

            @Override
            public void failure(TwitterException exception) {
                mMainPresenter.showToast("error");
                System.err.print(exception);
            }
        });
    }

    public void getFeedAdapter() {
        final StatusesService service = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> call = service
                .homeTimeline(10, null, null, null, null, null, null);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    mAdapter = new TweetAdapter(result.data);
                    mAdapter.notifyDataSetChanged();
                    mMainPresenter.updateData(mAdapter);
                } else {
                    mMainPresenter.showToast("error");
                }
            }

            @Override
            public void failure(TwitterException exception) {
                mMainPresenter.showToast("error");
                System.err.print(exception);
            }
        });
    }
}