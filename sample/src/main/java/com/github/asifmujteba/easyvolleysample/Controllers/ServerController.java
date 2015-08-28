package com.github.asifmujteba.easyvolleysample.Controllers;

import android.content.Context;

import com.github.asifmujteba.easyvolley.ASFBitmapRequest;
import com.github.asifmujteba.easyvolley.ASFRequestListener;
import com.github.asifmujteba.easyvolley.EasyVolley;
import com.github.asifmujteba.easyvolleysample.Models.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by asifmujteba on 08/08/15.
 */
public class ServerController {
    private static final String TAG = ServerController.class.getName().toString();

    private static final String URLBase = "https://www.zalora.com.my/mobile-api/";

    private static final String URLWomenClothing   = URLBase + "women/clothing/";

    public ServerController(Context context) {
        EasyVolley.initialize(context);
    }

    public void dispose() {
        EasyVolley.dispose();
    }

    public void unSubscribe(Object subscriber) {
        EasyVolley.unSubscribe(subscriber);
    }

    public interface OnServerResponseListener<T> {
        void onSuccess(T response);
        void onError(Exception e);
    }

    public ASFBitmapRequest loadImage(String url) {
        return EasyVolley.withGlobalQueue()
                .load(url)
                .asBitmap();
    }

    public void getWomenClothingProducts(Object subscriber,
                                     final OnServerResponseListener<ArrayList<Product>> listener) {
        EasyVolley.withGlobalQueue()
                .load(URLWomenClothing)
                .addParam("p1", "something")
                .asJsonObject()
                .setSubscriber(subscriber)
                .setCallback(new ASFRequestListener<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject response) {
                        JsonArray results = response.get("metadata").getAsJsonObject()
                                .getAsJsonArray("results");
                        ArrayList<Product> products = Product.parseJsonArray(results);
                        if (listener != null) {
                            listener.onSuccess(products);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                })
                .start();
    }
}
