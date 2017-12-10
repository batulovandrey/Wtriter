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
import java.util.concurrent.TimeUnit;

import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public class MainModel {

    private MainPresenter mMainPresenter;
    private TweetAdapter mAdapter;
    private final BehaviorSubject<String> mBehaviorSubject;
    private final SearchService mSearchService;

    public MainModel(MainPresenter mMainPresenter) {
        this.mMainPresenter = mMainPresenter;
        mAdapter = new TweetAdapter();
        mBehaviorSubject = BehaviorSubject.create();
        mSearchService = TwitterCore.getInstance().getApiClient().getSearchService();
    }

    public void getData(String query) {
        if (query == null) {
            getFeed();
        } else {
            mBehaviorSubject.onNext(query);
            getTweetsByQuery(query);
        }
        mMainPresenter.updateData(mAdapter);
    }

    private void getFeed() {
        final StatusesService service = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> call = service
                .homeTimeline(10, null, null, null, null, null, null);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    mAdapter.setTweets(result.data);
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

    private void getTweetsByQuery(String query) {
        mBehaviorSubject
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(q -> {
                    Call<Search> call = mSearchService.tweets(query, null, null, null, null, null, null, null, null, null);
                    call.enqueue(new retrofit2.Callback<Search>() {
                        @Override
                        public void onResponse(Call<Search> call, Response<Search> response) {
                            if (response != null && response.body() != null) {
                                mAdapter.setTweets(response.body().tweets);
                            } else {
                                mMainPresenter.showToast("error");
                            }
                        }

                        @Override
                        public void onFailure(Call<Search> call, Throwable t) {
                            mMainPresenter.showToast("error");
                            System.err.print(t);
                        }
                    });
                });
    }
}