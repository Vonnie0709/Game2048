package com.vonnie.game.v2048.listener;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author LongpingZou
 * @date 2018/7/2
 */
public class ShareListener implements UMShareListener {
    private Context context;

    public ShareListener(Context context) {
        this.context = context;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        String name = share_media.getName();
        if ("wxsession".equals(name)) {
            MobclickAgent.onEvent(context, "share_wechat");
        } else if ("wxtimeline".equals(name)) {
            MobclickAgent.onEvent(context, "share_wechat_circle");
        } else if ("sina".equals(name)) {
            MobclickAgent.onEvent(context, "share_weibo");
        } else if ("qq".equals(name)) {
            MobclickAgent.onEvent(context, "share_qq");
        } else if ("qzone".equals(name)) {
            MobclickAgent.onEvent(context, "share_qzone");
        }
        Log.i("ABC", "name:" + name);
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
