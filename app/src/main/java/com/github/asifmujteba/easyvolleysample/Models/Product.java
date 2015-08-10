/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.github.asifmujteba.easyvolleysample.Models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Product {
	ImageElement[] images;

 	public void setImages(ImageElement[] images) {
		this.images = images;
	}

	public ImageElement[] getImages() {
		return images;
	}

	Data data;

 	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	String id;

 	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

    public static ArrayList<Product> parseJsonArray(JsonArray jsonArray) {
        ArrayList<Product> products = new ArrayList<>(jsonArray.size());

        Gson gson = new Gson();
        for (int i=0 ; i<jsonArray.size() ; i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            Product product = gson.fromJson(jsonObject, Product.class);

            // temp hack for build
            for (int j=0 ; j<product.getImages().length ; j++) {
                product.getImages()[j].setPath(product.getImages()[j].getPath().replace("-catalogmobile", ""));
            }

            products.add(product);
        }

        return products;
    }
}