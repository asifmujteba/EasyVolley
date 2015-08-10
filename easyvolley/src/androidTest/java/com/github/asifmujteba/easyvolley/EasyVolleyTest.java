package com.github.asifmujteba.easyvolley;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.CountDownLatch;

/**
 * Created by asifmujteba on 09/08/15.
 */
public class EasyVolleyTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        EasyVolley.initialize(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        EasyVolley.dispose();
    }

    public void testCacheWorking() {
        // Pre
        String url = "http://google.com";
        final String res = "{\"res\": \"Cached Response\"}";
        final JsonObject jsonObject = (new Gson()).fromJson(res, JsonElement.class).getAsJsonObject();
        EasyVolley.getMemoryCache().put(url,
                new ASFCache.ASFEntry(jsonObject, res.length(), url, System.currentTimeMillis()));

        // Process
        final CountDownLatch signal = new CountDownLatch(1);
        EasyVolley.withGlobalQueue()
                .load(url)
                .asJsonObject()
                .setCallback(new ASFRequestListener<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject response) {
                        //Post
                        assertTrue(jsonObject.equals(response));
                        signal.countDown();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Post
                        fail();
                        signal.countDown();
                    }
                }).start();
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testJsonParsing() {
        // Pre
        String url = "http://google.com";
        final String res = "{\"results\": [\"Result 1\", \"Result 2\"]}";
        final JsonObject jsonObject = (new Gson()).fromJson(res, JsonElement.class).getAsJsonObject();
        EasyVolley.getMemoryCache().put(url,
                new ASFCache.ASFEntry(jsonObject, res.length(), url, System.currentTimeMillis()));

        // Process
        final CountDownLatch signal = new CountDownLatch(1);
        EasyVolley.withGlobalQueue()
                .load(url)
                .asJsonObject()
                .setCallback(new ASFRequestListener<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject response) {
                        //Post
                        assertTrue(jsonObject.equals(response));
                        signal.countDown();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Post
                        fail();
                        signal.countDown();
                    }
                }).start();
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
