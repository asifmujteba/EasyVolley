package com.github.asifmujteba.easyvolley;

/**
 * Created by asifmujteba on 07/08/15.
 */
public interface ASFRequestListener<T> {
    void onSuccess(T response);
    void onFailure(Exception e);
}
