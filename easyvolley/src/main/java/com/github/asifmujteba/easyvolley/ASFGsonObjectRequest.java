package com.github.asifmujteba.easyvolley;

import com.android.volley.Response;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by asifmujteba on 23/04/15.
 */
public class ASFGsonObjectRequest extends ASFGsonRequest<JsonObject> {
    private static final String TAG = ASFGsonObjectRequest.class.getName().toString();

    public ASFGsonObjectRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(method, url, JsonObject.class, headers, params, listener, errorListener);
    }
}
