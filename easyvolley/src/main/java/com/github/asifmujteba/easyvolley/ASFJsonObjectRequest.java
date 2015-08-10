package com.github.asifmujteba.easyvolley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class ASFJsonObjectRequest extends ASFRequest<JsonObject> {
    private static final String TAG = ASFJsonObjectRequest.class.getName().toString();

    private ASFGsonObjectRequest mRequest;

    private Response.Listener<JsonObject> mResponseListener = new Response.Listener<JsonObject>() {
        @Override
        public void onResponse(JsonObject response) {
            notifyResponseAndCacheIfNeeded(response);
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            notifyError(new Exception(error.getMessage()));
        }
    };

    public ASFJsonObjectRequest(ASFRequestQueue requestQueue) {
        super(requestQueue);
    }

    @Override
    protected Request getRequest() {
        if (mRequest == null) {
            mRequest = new ASFGsonObjectRequest(getMethod().getValue(), getUrl(), getHeaders(),
                    getParams(), mResponseListener, mErrorListener);
        }
        return mRequest;
    }

    @Override
    protected Response.Listener<JsonObject> getResponseListener() {
        return mResponseListener;
    }

    @Override
    protected Response.ErrorListener getErorListener() {
        return mErrorListener;
    }

    @Override
    protected ASFCache.ASFEntry createCacheEntry(JsonObject response) {
        return new ASFCache.ASFEntry(response, response.toString().length(), getUrl(),
                System.currentTimeMillis());
    }
}
