package com.github.butul0ve.wtriter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.butul0ve.wtriter.adapter.TweetAdapter;
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
 * @author Andrey Batulov on 06.12.17
 */

public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_QUERY = "param1";

    private String mQuery;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TweetAdapter mAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(EXTRA_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mQuery != null && mQuery.length() > 0) {
            loadTweets(mQuery);
        } else {
            loadTweetsFromFeed();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewData();
            }
        });
    }

    private void loadRecyclerViewData() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (mQuery != null) {
            loadTweets(mQuery);
        } else {
            loadTweetsFromFeed();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadTweets(String query) {
        final SearchService service = TwitterCore.getInstance().getApiClient().getSearchService();
        Call<Search> call = service.tweets(query, null, null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                if (result != null) {
                    mAdapter = new TweetAdapter(result.data.tweets);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadTweetsFromFeed() {
        final StatusesService service = TwitterCore.getInstance().getApiClient().getStatusesService();

        Call<List<Tweet>> call = service
                .homeTimeline(10, null, null, null, null, null, null);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    mAdapter = new TweetAdapter(result.data);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}