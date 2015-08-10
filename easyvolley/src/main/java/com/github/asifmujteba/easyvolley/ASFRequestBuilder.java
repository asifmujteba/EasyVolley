package com.github.asifmujteba.easyvolley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class ASFRequestBuilder {
    private ASFRequestContext mRequestContext;
    private Map<String, String> mHeaders = new HashMap<>();
    private Map<String, String> mParams = new HashMap<>();

    public ASFRequestBuilder(ASFRequestContext requestContext) {
        this.mRequestContext = requestContext;
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    public void addParam(String key, String value) {
        mParams.put(key, value);
    }

    public ASFBitmapRequest asBitmap() {
        ASFBitmapRequest request = new ASFBitmapRequest(mRequestContext.getQueue());
        initialize(request);
        return request;
    }

    public ASFJsonObjectRequest asJsonObject() {
        ASFJsonObjectRequest request = new ASFJsonObjectRequest(mRequestContext.getQueue());
        initialize(request);
        return request;
    }

    private void initialize(ASFRequest request) {
        request.setMethod(mRequestContext.getMethod());
        request.setUrl(mRequestContext.getUrl());
        request.getHeaders().putAll(mHeaders);
        request.getParams().putAll(mParams);
        request.setCache(EasyVolley.getMemoryCache());
    }
}
