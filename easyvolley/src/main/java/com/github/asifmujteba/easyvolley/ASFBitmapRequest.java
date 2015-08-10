package com.github.asifmujteba.easyvolley;

import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.ref.WeakReference;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class ASFBitmapRequest extends ASFRequest<Bitmap> {
    private static final String TAG = ASFBitmapRequest.class.getName().toString();

    public static final int DEFAULT_WIDTH  = 0;
    public static final int DEFAULT_HEIGHT = 0;

    private com.android.volley.toolbox.ImageRequest mRequest;
    private int mMaxWidth = DEFAULT_WIDTH, mMaxHeight = DEFAULT_HEIGHT;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER;
    private Bitmap.Config mDecodeConfig = null;
    private WeakReference<ImageView> imageViewWeakReference = null;

    private Response.Listener<Bitmap> mResponseListener = new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap response) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null && imageView.getTag(R.string.urlTagKey)
                    .equals(getUrl())) {
                imageView.setImageBitmap(response);
            }

            notifyResponseAndCacheIfNeeded(response);
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            notifyError(new Exception(error.getMessage()));
        }
    };


    public ASFBitmapRequest(ASFRequestQueue requestQueue) {
        super(requestQueue);
    }

    @Override
    protected Request getRequest() {
        if (mRequest == null) {
            mRequest = new com.android.volley.toolbox.ImageRequest(getUrl(), mResponseListener,
                    getMaxWidth(), getMaxHeight(), getScaleType(), getDecodeConfig(),
                    mErrorListener);
        }
        return mRequest;
    }

    @Override
    protected Response.Listener<Bitmap> getResponseListener() {
        return mResponseListener;
    }

    @Override
    protected Response.ErrorListener getErorListener() {
        return mErrorListener;
    }

    @Override
    protected ASFCache.ASFEntry createCacheEntry(Bitmap response) {
        return new ASFCache.ASFEntry(response, getBitmapSize(response), getUrl(),
                System.currentTimeMillis());
    }

    private int getBitmapSize(Bitmap response) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return response.getAllocationByteCount();
        }
        else {
            return response.getByteCount();
        }
    }

    private int getMaxWidth() {
        return mMaxWidth;
    }

    private int getMaxHeight() {
        return mMaxHeight;
    }

    private ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    private Bitmap.Config getDecodeConfig() {
        return mDecodeConfig;
    }

    public ASFBitmapRequest setMaxWidth(int mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
        return this;
    }

    public ASFBitmapRequest setMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
        return this;
    }

    public ASFBitmapRequest setScaleType(ImageView.ScaleType mScaleType) {
        this.mScaleType = mScaleType;
        return this;
    }

    public ASFBitmapRequest setDecodeConfig(Bitmap.Config mDecodeConfig) {
        this.mDecodeConfig = mDecodeConfig;
        return this;
    }

    public ASFBitmapRequest into(ImageView imageView) {
        imageView.setTag(R.string.urlTagKey, getUrl());
        imageViewWeakReference = new WeakReference<>(imageView);
        return this;
    }
}
