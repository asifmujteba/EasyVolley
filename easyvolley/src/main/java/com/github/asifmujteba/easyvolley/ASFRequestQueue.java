package com.github.asifmujteba.easyvolley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asifmujteba on 08/08/15.
 */
public class ASFRequestQueue {
    private static final String TAG = ASFRequestQueue.class.getName().toString();

    private RequestQueue mRequestQueue;
    private ArrayList<WeakReference<ASFRequest>> mCurrentRequests;

    protected ASFRequestQueue(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
        this.mCurrentRequests = new ArrayList<>();
    }

    protected void add(Request request) {
        mRequestQueue.add(request);
    }

    protected void addToCurrentRequests(ASFRequest request) {
        boolean found = false;
        for (Iterator<WeakReference<ASFRequest>> iterator = mCurrentRequests.iterator();
             iterator.hasNext();) {
            WeakReference<ASFRequest> weakRef = iterator.next();
            if (weakRef.get() == request) {
                found = true;
                break;
            }
        }

        if (!found) {
            mCurrentRequests.add(new WeakReference<>(request));
        }
    }

    protected void removeFromCurrentRequests(ASFRequest request) {
        for (Iterator<WeakReference<ASFRequest>> iterator = mCurrentRequests.iterator();
             iterator.hasNext();) {
            WeakReference<ASFRequest> weakRef = iterator.next();
            if (weakRef.get() == request) {
                iterator.remove();
            }
        }
    }

    protected int getCurrentRequestsCount() {
        return mCurrentRequests.size();
    }

    protected ArrayList<WeakReference<ASFRequest>> getCurrentRequests() {
        return mCurrentRequests;
    }

    protected void start() {
        mRequestQueue.start();
    }

    protected void stop() {
        mRequestQueue.stop();
    }
}
