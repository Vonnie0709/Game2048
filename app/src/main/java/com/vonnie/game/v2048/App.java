package com.vonnie.game.v2048;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author longp
 * @date 2018/6/26
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
