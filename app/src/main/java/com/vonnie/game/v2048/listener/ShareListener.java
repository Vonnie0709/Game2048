package com.vonnie.game.v2048.listener;

import android.util.Log;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author LongpingZou
 * @date 2018/7/2
 */
public class ShareListener implements UMShareListener {
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        String name = share_media.getName();
        Log.i("ABC", "name:" + name);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
