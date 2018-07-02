package com.vonnie.game.v2048.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.utils.BitmapUtil;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.ShareGameView;

/**
 * @author LongpingZou
 * @date 2018/7/2
 */
public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
    }

    private void initView() {
        mContainer = findViewById(R.id.game_container);

        /**
         * load last game data
         */
        loadHighScore();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        findViewById(R.id.share_commit).setOnClickListener(this);
    }

    private void loadHighScore() {
        int mode = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_MODE, 0);
        int x = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_X_NUM, 4);
        int y = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_Y_NUM, 4);
        ShareGameView view = new ShareGameView(this, x, y, mode);
        mContainer.addView(view, 0);
        for (int xx = 0; xx < view.grid.field.length; xx++) {
            for (int yy = 0; yy < view.grid.field[0].length; yy++) {
                int value = (int) SharedPreferenceUtil.get(this, "high" + xx + "_" + yy, -1);
                if (value > 0) {
                    view.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    view.grid.field[xx][yy] = null;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_commit:
                Bitmap bitmap = BitmapUtil.loadBitmapFromView(mContainer);
                UMImage image = new UMImage(ShareActivity.this, bitmap);
                new ShareAction(this).withText(getString(R.string.share_high_score_tips)).withMedia(image).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN).setCallback(shareListener).open();
                break;
            default:
                break;
        }
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this, "成功 了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareActivity.this, "失 败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this, "取消 了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
