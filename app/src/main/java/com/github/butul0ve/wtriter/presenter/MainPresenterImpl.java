package com.github.butul0ve.wtriter.presenter;

import com.github.butul0ve.wtriter.adapter.TweetAdapter;
import com.github.butul0ve.wtriter.model.MainModel;
import com.github.butul0ve.wtriter.view.MainView;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainModel mMainModel;
    private MainView mMainView;

    public MainPresenterImpl(MainView mainView) {
        mMainView = mainView;
        mMainModel = new MainModel(this);
    }

    @Override
    public void showToast(String text) {
        mMainView.showToast(text);
    }

    @Override
    public void getData(String query) {
        mMainModel.getData(query);
    }

    @Override
    public void updateData(TweetAdapter adapter) {
        mMainView.updateData(adapter);
    }
}