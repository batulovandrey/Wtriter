package com.github.butul0ve.wtriter.view;

import com.github.butul0ve.wtriter.adapter.TweetAdapter;

/**
 * @author Andrey Batulov on 07.12.17.
 */

public interface MainView {

    void showToast(String text);

    void updateData(TweetAdapter adapter);
}