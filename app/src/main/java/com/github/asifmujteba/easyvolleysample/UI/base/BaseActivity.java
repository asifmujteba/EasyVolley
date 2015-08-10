package com.github.asifmujteba.easyvolleysample.UI.base;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.asifmujteba.easyvolleysample.R;

import butterknife.ButterKnife;

/**
 * Created by asifmujteba on 07/08/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseActivityDelegate {
    private Toolbar toolbar;
    protected BaseActivity mContext = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowFlags();
        setContentView(getLayoutResource());
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (showUpButton()) {
                final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
            else {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressBar();
    }

    protected abstract int getLayoutResource();

    protected void setWindowFlags() {}

    protected boolean showUpButton() {
        return !isTaskRoot();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public View getStatusBar() {
        View v = findViewById(android.R.id.statusBarBackground);
        return v;
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    protected void setActionBarColor(int colorRes) {
        toolbar.setBackgroundDrawable(new ColorDrawable(colorRes));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home: {
                if (supportTransitionOnBack()) {
                    supportFinishAfterTransition();
                }
                else {
                    finish();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean supportTransitionOnBack() {
        return false;
    }

    @Override
    public void showProgressBar() {
        showProgressBar(getString(R.string.please_wait));
    }

    public void showProgressBar(String text) {
        hideProgressBar();
        pDialog = ProgressDialog.show(this, "", text, false, false);
    }

    @Override
    public void showProgressBar(int textId) {
        showProgressBar(getString(textId));
    }

    public void hideProgressBar() {
        if (pDialog != null && pDialog.isShowing()) { pDialog.dismiss(); }
    }
}
