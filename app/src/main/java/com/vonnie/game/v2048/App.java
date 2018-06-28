package com.vonnie.game.v2048;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vonnie.game.v2048.constant.Constants;

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
        initUMeng();
    }

    private void initUMeng() {
        Log.i("ABC", getChannel(getApplicationContext()));
        UMConfigure.init(this, Constants.UM_APP_KEY, getChannel(getApplicationContext()), UMConfigure.DEVICE_TYPE_PHONE, null);
//        PlatformConfig.setWeixin("","");
//        PlatformConfig.setSinaWeibo();
    }

    private String getChannel(Context context) {
        String channel = "";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
            ignored.printStackTrace();
        }
        if (channel == null || channel.isEmpty()) {
            channel = "1007";
        }
        return channel;
    }
}
