package com.github.asifmujteba.easyvolley;

import android.content.Context;
import android.util.Pair;

import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class EasyVolley {
    private static final String TAG = EasyVolley.class.getName().toString();

    private static final int DEFAULT_MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024;

    private static com.github.asifmujteba.easyvolley.ASFRequestQueue mGlobalRequestQueue;
    private static ArrayList<com.github.asifmujteba.easyvolley.ASFRequestQueue> mRequestQueues = new ArrayList<>();
    private static ArrayList<Pair<WeakReference<Object>, WeakReference<com.github.asifmujteba.easyvolley.ASFRequest>>> subscribers
            = new ArrayList<>();
    private static com.github.asifmujteba.easyvolley.ASFMemoryCache mMemoryCache;
    private static int maxDiskCacheBytes = DEFAULT_MAX_DISK_CACHE_SIZE;

    public static void initialize(Context appContext) {
        initialize(appContext, 0);
    }

    public static void initialize(Context appContext, int lruCacheSize) {
        if (mGlobalRequestQueue == null) {
            mGlobalRequestQueue = new com.github.asifmujteba.easyvolley.ASFRequestQueue(Volley.newRequestQueue(appContext,
                    new HurlStack(), maxDiskCacheBytes));
        }

        if (lruCacheSize > 0) {
            getMemoryCache().setMaxSize(lruCacheSize);
        }
    }

    public static void dispose() {
        if (mGlobalRequestQueue != null) {
            mGlobalRequestQueue.stop();
            mGlobalRequestQueue = null;
        }

        for (Iterator<com.github.asifmujteba.easyvolley.ASFRequestQueue> iterator = mRequestQueues.iterator();
             iterator.hasNext();) {
            com.github.asifmujteba.easyvolley.ASFRequestQueue requestQueue = iterator.next();
            requestQueue.stop();
            iterator.remove();
        }

        if (mMemoryCache != null) {
            mMemoryCache.clear();
            mMemoryCache = null;
        }
    }

    public static com.github.asifmujteba.easyvolley.ASFRequestContext withGlobalQueue() {
        if (mGlobalRequestQueue == null) {
            throw new RuntimeException("You need to call initialize() first");
        }

        return new com.github.asifmujteba.easyvolley.ASFRequestContext(mGlobalRequestQueue);
    }

    public static com.github.asifmujteba.easyvolley.ASFRequestContext withNewQueue(Context context) {
        return new com.github.asifmujteba.easyvolley.ASFRequestContext(new ASFRequestQueue(Volley.newRequestQueue(context,
                new HurlStack())));
    }

    protected static com.github.asifmujteba.easyvolley.ASFCache getMemoryCache() {
        if (mMemoryCache == null) {
            mMemoryCache = new com.github.asifmujteba.easyvolley.ASFMemoryCache();
        }
        return mMemoryCache;
    }

    protected static void addedRequest(com.github.asifmujteba.easyvolley.ASFRequest request) {
        ASFRequestQueue requestQueue = request.getRequestQueue();
        requestQueue.addToCurrentRequests(request);
        if (requestQueue != mGlobalRequestQueue) {
            synchronized (mRequestQueues) {
                if (!mRequestQueues.contains(requestQueue)) {
                    mRequestQueues.add(requestQueue);
                }
            }
        }
    }

    protected static void removedRequest(ASFRequest request) {
        ASFRequestQueue requestQueue = request.getRequestQueue();
        requestQueue.removeFromCurrentRequests(request);
        if (requestQueue != mGlobalRequestQueue && requestQueue.getCurrentRequestsCount() == 0) {
            synchronized (mRequestQueues) {
                if (mRequestQueues.contains(requestQueue)) {
                    mRequestQueues.remove(requestQueue);
                }
            }
        }

        for (Iterator<Pair<WeakReference<Object>, WeakReference<ASFRequest>>> iterator
             = subscribers.iterator(); iterator.hasNext();) {
            Pair<WeakReference<Object>, WeakReference<ASFRequest>> pair = iterator.next();
            if (pair.second.get() != null && pair.second.get() == request) {
                iterator.remove();
            }
        }
    }

    protected static void subscribe(Object object, ASFRequest request) {
        Pair pair = createSubscriptionPair(object, request);
        synchronized (subscribers) {
            if (!subscribers.contains(pair)) {
                subscribers.add(pair);
            }
        }
    }

    public static void unSubscribe(Object object) {
        for (Iterator<Pair<WeakReference<Object>, WeakReference<ASFRequest>>> iterator
             = subscribers.iterator(); iterator.hasNext();) {
            Pair<WeakReference<Object>, WeakReference<ASFRequest>> pair = iterator.next();
            if (pair.first.get() != null && pair.first.get() == object) {
                ASFRequest request = pair.second.get();
                if (request != null && !request.isCanceled()) {
                    request.cancel();
                }

                iterator.remove();
            }
        }
    }

    protected static Pair<WeakReference<Object>, WeakReference<ASFRequest>> createSubscriptionPair(
            Object object, ASFRequest request) {
        return new Pair<>(new WeakReference<>(object), new WeakReference<>(request));
    }
}
