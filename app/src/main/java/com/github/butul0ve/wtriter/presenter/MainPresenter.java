package com.github.butul0ve.wtriter.presenter;

import com.github.butul0ve.wtriter.adapter.TweetAdapter;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public interface MainPresenter {

    void showToast(String text);

    void getData(String query);

    void updateData(TweetAdapter adapter);
}