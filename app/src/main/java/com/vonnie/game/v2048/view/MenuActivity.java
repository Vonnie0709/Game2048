package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vonnie.game.v2048.App;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.constant.Constants;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.listener.ShareListener;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.CustomBottomDialog;

/**
 * @author LongpingZou
 * @date 2018/6/25
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnHighScore;

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
        btnHighScore = findViewById(R.id.menu_high_score);
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
                setResult(Constants.RESULT_CODE_RESUME);
                finish();
                break;
            case R.id.menu_new_game:
                setResult(Constants.RESULT_CODE_NEW_GAME);
                finish();
                break;
            case R.id.menu_mode:
                intent = new Intent(this, ModeActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_share:
                UMImage umImage = new UMImage(this, App.getContext().shareBitmap);
                new ShareAction(this).withText(getString(R.string.share_normal_tips)).withMedia(umImage).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(new ShareListener()).open();
                break;
            case R.id.menu_high_score:
                intent = new Intent(MenuActivity.this, ShareActivity.class);
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
