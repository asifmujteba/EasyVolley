package com.github.asifmujteba.easyvolley;

import java.lang.ref.WeakReference;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class ASFRequestContext {

    private WeakReference<ASFRequestQueue> mRequestQueueWeakReference;
    private String mUrl;
    private ASFRequest.METHOD mMethod;

    public ASFRequestContext(ASFRequestQueue requestQueue) {
        this.mRequestQueueWeakReference = new WeakReference<>(requestQueue);
    }

    public ASFRequestBuilder load(String url) {
        this.mUrl = url;
        this.mMethod = ASFRequest.METHOD.GET;
        return new ASFRequestBuilder(this);
    }

    public ASFRequestBuilder load(ASFRequest.METHOD method, String url) {
        this.mMethod = method;
        this.mUrl = url;
        return new ASFRequestBuilder(this);
    }

    protected String getUrl() {
        return mUrl;
    }

    protected ASFRequest.METHOD getMethod() {
        return mMethod;
    }

    protected ASFRequestQueue getQueue() {
        return mRequestQueueWeakReference.get();
    }
}
