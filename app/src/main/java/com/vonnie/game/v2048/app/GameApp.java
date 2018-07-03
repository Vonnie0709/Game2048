package com.vonnie.game.v2048.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.Constants;

/**
 * @author LongpingZou
 * @date 2018/6/26
 */
public class GameApp extends Application {
    private static GameApp instance;
    public Tile[][] field;
    public int gameMode;
    public int numX;
    public int numY;
    public int score;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUMeng();
    }

    public static GameApp getContext() {
        return instance;
    }

    private void initUMeng() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, Constants.UM_APP_KEY, getChannel(getApplicationContext()), UMConfigure.DEVICE_TYPE_PHONE, null);
        PlatformConfig.setSinaWeibo(Constants.WEIBO_APP_KEY, Constants.WEIBO_SECRET_KEY, "http://sns.whalecloud.com");
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
        PlatformConfig.setWeixin(Constants.WECHAT_APP_ID, Constants.WECHAT_SECRET_KEY);
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
