package com.vonnie.game.v2048.view;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * @author LongpingZou
 * @date 2018/7/3
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
