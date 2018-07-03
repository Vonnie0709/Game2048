package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.constant.Constants;
import com.vonnie.game.v2048.constant.IntentConstant;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;

/**
 * @author LongpingZou
 * @date 2018/6/25
 */
public class MenuActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
    }

    private void initView() {
        findViewById(R.id.menu_resume).setOnClickListener(this);
        findViewById(R.id.menu_new_game).setOnClickListener(this);
        findViewById(R.id.menu_mode).setOnClickListener(this);
        findViewById(R.id.menu_share).setOnClickListener(this);
        Button btnHighScore = findViewById(R.id.menu_high_score);
        btnHighScore.setOnClickListener(this);
        int highScore = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE, 0);
        if (highScore > 0) {
            btnHighScore.setVisibility(View.VISIBLE);
        } else {
            btnHighScore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_resume:
                MobclickAgent.onEvent(this, "menu_click_back_game");
                setResult(Constants.RESULT_CODE_RESUME);
                finish();
                break;
            case R.id.menu_new_game:
                MobclickAgent.onEvent(this, "menu_click_new_game");
                setResult(Constants.RESULT_CODE_NEW_GAME);
                finish();
                break;
            case R.id.menu_mode:
                MobclickAgent.onEvent(this, "menu_click_mode_select");
                intent = new Intent(this, ModeActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_share:
                MobclickAgent.onEvent(this, "menu_click_share");
                intent = new Intent(MenuActivity.this, ShareActivity.class);
                //normal share
                intent.putExtra(IntentConstant.INTENT_SHARE_TYPE, 0);
                startActivity(intent);
                break;
            case R.id.menu_high_score:
                MobclickAgent.onEvent(this, "menu_click_high_score");
                //high score share
                intent = new Intent(MenuActivity.this, ShareActivity.class);
                intent.putExtra(IntentConstant.INTENT_SHARE_TYPE, 1);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(Constants.RESULT_CODE_MODE_CHOOSE, data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
