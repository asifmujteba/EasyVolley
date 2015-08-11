package com.github.asifmujteba.easyvolleysample.UI.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.github.asifmujteba.easyvolley.ASFRequestListener;
import com.github.asifmujteba.easyvolleysample.App;
import com.github.asifmujteba.easyvolleysample.Models.Product;
import com.github.asifmujteba.easyvolleysample.R;
import com.github.asifmujteba.easyvolleysample.UI.base.BaseActivity;

import org.parceler.Parcels;

import butterknife.InjectView;

public class DetailsActivity extends BaseActivity {

    private static final String EXTRA_PRODUCT = "Extra_Product";

    protected @InjectView(R.id.imageView) ImageView imageView;

    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_PRODUCT)) {
            product = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_PRODUCT));
        }

        if (product.getImages().length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                postponeEnterTransition();
            }

            App.getInstance().getServerController().loadImage(product.getImages()[0].getPath())
                    .setScaleType(ImageView.ScaleType.CENTER_INSIDE)
                    .into(imageView)
                    .setCallback(new ASFRequestListener<Bitmap>() {
                        @Override
                        public void onSuccess(Bitmap response) {
                            scheduleStartPostponedTransition(imageView);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            scheduleStartPostponedTransition(imageView);
                        }
                    })
                    .start();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_details;
    }

    @Override
    public boolean supportTransitionOnBack() {
        return true;
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    public static void launch(BaseActivity mContext, Product product, ImageView imageView) {
        Intent intent = new Intent(mContext, DetailsActivity.class);
        intent.putExtra(EXTRA_PRODUCT, Parcels.wrap(product));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(mContext, imageView, "productImage");
        ActivityCompat.startActivity(mContext, intent, options.toBundle());
    }
}
