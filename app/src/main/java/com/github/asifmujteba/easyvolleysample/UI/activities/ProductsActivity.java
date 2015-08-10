package com.github.asifmujteba.easyvolleysample.UI.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.asifmujteba.easyvolleysample.App;
import com.github.asifmujteba.easyvolleysample.Controllers.ServerController;
import com.github.asifmujteba.easyvolleysample.Models.Product;
import com.github.asifmujteba.easyvolleysample.R;
import com.github.asifmujteba.easyvolleysample.UI.adapters.ProductsAdapter;
import com.github.asifmujteba.easyvolleysample.UI.base.BaseActivity;

import java.util.ArrayList;

import butterknife.InjectView;

public class ProductsActivity extends BaseActivity {
    private static final String TAG = ProductsActivity.class.getName().toString();

    protected @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    protected @InjectView(R.id.gridView) GridView gridView;
    protected @InjectView(R.id.emptyElement) TextView emptyElement;

    protected ProductsAdapter productsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsAdapter = new ProductsAdapter(mContext, new ArrayList<Product>());
        gridView.setEmptyView(emptyElement);
        gridView.setAdapter(productsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productsAdapter.getItem(position);
                DetailsActivity.launch(mContext, product, (ImageView) view.findViewById(R.id.imageView));
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProducts();
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        emptyElement.setText(R.string.loading);
        showProgressBar();
        App.getInstance().getServerController().getWomenClothingProducts(mContext,
                new ServerController.OnServerResponseListener<ArrayList<Product>>() {
                    @Override
                    public void onSuccess(ArrayList<Product> response) {
                        emptyElement.setText(R.string.no_data_pull);
                        swipeContainer.setRefreshing(false);
                        hideProgressBar();
                        productsAdapter.setProductsAndNotify(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        emptyElement.setText(R.string.no_data_pull);
                        swipeContainer.setRefreshing(false);
                        hideProgressBar();
                        App.showToast(e.getMessage());
                    }
                });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        App.getInstance().getServerController().unSubscribe(this);
        super.onDestroy();
    }
}
