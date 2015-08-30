package com.github.asifmujteba.easyvolley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by asifmujteba on 23/04/15.
 */
public abstract class ASFGsonRequest<T> extends Request<T> {
    private static final String TAG = ASFGsonRequest.class.getName().toString();

    private final Gson gson ;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    public ASFGsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                          Map<String, String> params,
                          Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, (method==Method.GET)?appendParamsToURL(url, params):url, errorListener);

        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
        this.clazz = clazz;
        this.headers = headers;
        this.params = params;
        this.listener = listener;


    }

    private static String appendParamsToURL(String url, Map<String, String> params) {
        if (!url.contains("?")) {
            url += "?";
        }

        for (Map.Entry<String, String> entry : params.entrySet())  {
             if (!url.endsWith("?")) {
                url += "&";
            }

            try {
                url += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                url += entry.getKey() + "=" + URLEncoder.encode(entry.getValue());
            }
        }

        return url;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
