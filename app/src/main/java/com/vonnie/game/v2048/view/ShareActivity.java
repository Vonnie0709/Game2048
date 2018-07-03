package com.vonnie.game.v2048.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.app.GameApp;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.IntentConstant;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.listener.ShareListener;
import com.vonnie.game.v2048.utils.BitmapUtil;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.ShareGameView;

/**
 * @author LongpingZou
 * @date 2018/7/2
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mContainer;
    private TextView mShareScore;
    private TextView mShareTitle;
    private TextView mShareDeclaration;
    private int shareDeclarationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
    }

    private int shareType;

    private void initView() {
        shareType = getIntent().getIntExtra(IntentConstant.INTENT_SHARE_TYPE, 0);
        mContainer = findViewById(R.id.game_container);
        mShareScore = findViewById(R.id.share_score);
        mShareTitle = findViewById(R.id.share_score_title);
        mShareDeclaration = findViewById(R.id.share_declaration);
        findViewById(R.id.share_commit).setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        switch (shareType) {
            case 0:
            case 2:
            case 3:
                shareDeclarationId = R.string.share_normal_tips;
                showNormalShare();
                break;
            case 1:
                shareDeclarationId = R.string.share_high_score_tips;
                showHighScoreShare();
                break;
            default:
                break;
        }


    }


    private void showNormalShare() {
        mShareTitle.setText(R.string.share_current_score);
        mShareDeclaration.setText(shareDeclarationId);
        loadGameView();
    }

    private void showHighScoreShare() {
        mShareTitle.setText(R.string.share_history_high_score);
        mShareDeclaration.setText(shareDeclarationId);
        int highScore = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE, 0);
        mShareScore.setText(String.valueOf(highScore));
        /**
         * load last game data
         */
        loadGameView();
    }


    private void loadGameView() {
        if (shareType == 1) {
            int gameMode = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_MODE, 0);
            int x = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_X_NUM, 0);
            int y = (int) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_Y_NUM, 0);
            ShareGameView view = new ShareGameView(this, x, y, gameMode);
            mContainer.addView(view, 1);
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
        } else {
            GameApp app = GameApp.getContext();
            int gameMode = app.gameMode;
            int x = app.numX;
            int y = app.numY;
            Tile[][] field = app.field;
            mShareScore.setText(String.valueOf(app.score));
            ShareGameView view = new ShareGameView(this, x, y, gameMode);
            mContainer.addView(view, 1);
            for (int xx = 0; xx < view.grid.field.length; xx++) {
                for (int yy = 0; yy < view.grid.field[0].length; yy++) {
                    view.grid.field[xx][yy] = field[xx][yy];
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_commit:
                MobclickAgent.onEvent(this, "share_click_commit");
                Bitmap bitmap = BitmapUtil.loadBitmapFromView(mContainer);
                UMImage image = new UMImage(ShareActivity.this, bitmap);
                new ShareAction(this).withText(getString(shareDeclarationId)).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE).setCallback(new ShareListener(this)).open();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
