package com.github.asifmujteba.easyvolleysample;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.github.asifmujteba.easyvolleysample.Controllers.ServerController;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    private ServerController serverController;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (this.serverController == null) {
            this.serverController = new ServerController(getApplicationContext());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.serverController.dispose();
        this.serverController = null;
    }

    public static void showToast(int id) {
        String szValue = instance.getString(id);
        Toast inst = Toast.makeText(getContext(), szValue, Toast.LENGTH_SHORT);
        inst.show();
    }

    public static void showToast(String szValue) {
        Toast inst = Toast.makeText(getContext(), szValue, Toast.LENGTH_SHORT);
        inst.show();
    }

    public ServerController getServerController() {
        return this.serverController;
    }
}
