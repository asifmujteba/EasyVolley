package com.github.asifmujteba.easyvolleysample.UI.base;

/**
 * Created by asifmujteba on 24/02/2014.
 */
public interface BaseActivityDelegate {

    public void showProgressBar();
    public void showProgressBar(String text);
    public void showProgressBar(int textId);
    public void hideProgressBar();
}
