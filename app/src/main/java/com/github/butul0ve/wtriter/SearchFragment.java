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
import com.github.butul0ve.wtriter.presenter.MainPresenter;
import com.github.butul0ve.wtriter.presenter.MainPresenterImpl;
import com.github.butul0ve.wtriter.view.MainView;

/**
 * @author Andrey Batulov on 06.12.17
 */

public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainView {

    private static final String EXTRA_QUERY = "param1";

    private String mQuery;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainPresenter mMainPresenter;

    public SearchFragment() {
        // Required empty public constructor
        mMainPresenter = new MainPresenterImpl(this);
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
        mMainPresenter.getData(mQuery);
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

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateData(TweetAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    private void loadRecyclerViewData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mMainPresenter.getData(mQuery);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}