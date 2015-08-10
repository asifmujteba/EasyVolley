package com.github.asifmujteba.easyvolley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asifmujteba on 07/08/15.
 */
public abstract class ASFRequest<T> {
    private static final String TAG = ASFRequest.class.getName().toString();

    public static final int DEFAULT_NETWORK_TIMEOUT_TIME = 1000 * 20;

    public enum METHOD {
        GET(Request.Method.GET), POST(Request.Method.POST);
        private int value;
        METHOD(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private WeakReference<ASFRequestQueue> mRequestQueueWeakReference;
    private ASFRequestListener<T> mCallback;
    private METHOD mMethod;
    private String mUrl;
    private WeakReference<ASFCache> mCache;
    private boolean mShouldUseCache = true;
    private int mNetworkTimeoutTime = DEFAULT_NETWORK_TIMEOUT_TIME;
    private final Map<String, String> mHeaders = new HashMap<>();
    private final Map<String, String> mParams = new HashMap<>();
    private boolean canceled;

    public ASFRequest(ASFRequestQueue requestQueue) {
        this.mRequestQueueWeakReference = new WeakReference<>(requestQueue);
    }

    public void start() {
        if (mRequestQueueWeakReference.get() != null && getRequest() != null) {
            if (shouldUseCache() && getCache() != null) {
                ASFCache.ASFEntry entry = getCache().get(getUrl());
                if (entry != null) {
                    try {
                        getResponseListener().onResponse((T) entry.getData());
                        return;
                    }catch (ClassCastException e) {}
                }
            }

            getRequest().setShouldCache(false);// since we need to use our own cache :)
            getRequest().setRetryPolicy(new DefaultRetryPolicy(
                    mNetworkTimeoutTime,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueueWeakReference.get().add(getRequest());
            mRequestQueueWeakReference.get().start();
            EasyVolley.addedRequest(this);
        }
    }

    public ASFRequest setSubscriber(Object object) {
        EasyVolley.subscribe(object, this);
        return this;
    }

    public ASFRequest setCallback(ASFRequestListener<T> listener) {
        this.mCallback = listener;
        return this;
    }

    public void cancel() {
        canceled = true;
        getRequest().cancel();
        setCallback(null);
        EasyVolley.removedRequest(this);
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean shouldUseCache() {
        return mShouldUseCache;
    }

    public void noCache() {
        mShouldUseCache = false;
    }

    public void setTimeout(int timeout) {
        mNetworkTimeoutTime = timeout;
    }

    protected String getUrl() {
        return mUrl;
    }

    protected METHOD getMethod() {
        return mMethod;
    }

    protected Map<String, String> getHeaders() {
        return mHeaders;
    }

    protected Map<String, String> getParams() {
        return mParams;
    }

    protected ASFCache getCache() {
        if (mCache != null) {
            return mCache.get();
        }
        return null;
    }

    protected void setCache(ASFCache cache) {
        this.mCache = new WeakReference<ASFCache>(cache);
    }

    protected void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    protected void setMethod(METHOD method) {
        this.mMethod = method;
    }

    protected void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    protected void addParam(String key, String value) {
        mParams.put(key, value);
    }

    protected ASFRequestQueue getRequestQueue() {
        return mRequestQueueWeakReference.get();
    }

    protected void notifyError(Exception e) {
        if (mCallback != null) {
            mCallback.onFailure(e);
            setCallback(null);
        }
        EasyVolley.removedRequest(this);
    }

    protected void notifyResponseAndCacheIfNeeded(T response) {
        if (shouldUseCache() && getCache() != null) {
            getCache().put(getUrl(), createCacheEntry(response));
        }

        notifyResponse(response);
    }

    protected abstract Request getRequest();
    protected abstract Response.Listener<T> getResponseListener();
    protected abstract Response.ErrorListener getErorListener();
    protected abstract ASFCache.ASFEntry createCacheEntry(T response);

    private void notifyResponse(T response) {
        if (mCallback != null) {
            mCallback.onSuccess(response);
            setCallback(null);
        }
        EasyVolley.removedRequest(this);
    }
}
