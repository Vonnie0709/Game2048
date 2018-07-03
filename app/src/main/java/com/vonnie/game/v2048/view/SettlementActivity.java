package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.constant.Constants;
import com.vonnie.game.v2048.constant.IntentConstant;

import static com.vonnie.game.v2048.logic.GameController.GAME_ENDLESS;
import static com.vonnie.game.v2048.logic.GameController.GAME_ENDLESS_WON;
import static com.vonnie.game.v2048.logic.GameController.GAME_LOST;
import static com.vonnie.game.v2048.logic.GameController.GAME_WIN;

/**
 * @author LongpingZou
 * @date 2018/6/23
 */
public class SettlementActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        int gameState = getIntent().getIntExtra(IntentConstant.INTENT_GAME_STATUS, 0);
        int score = getIntent().getIntExtra(IntentConstant.INTENT_SCORE, 0);
        TextView mGameInfo = findViewById(R.id.settlement_game_info);
        TextView mScore = findViewById(R.id.settlement_game_score);
        Button btnContinue = findViewById(R.id.settlement_game_continue);
        mScore.setText(String.valueOf(score));
        switch (gameState) {
            case GAME_WIN:
            case GAME_ENDLESS:
                mGameInfo.setText(R.string.settlement_win);
                btnContinue.setVisibility(View.VISIBLE);
                break;
            case GAME_LOST:
                mGameInfo.setText(R.string.settlement_game_over);
                btnContinue.setVisibility(View.GONE);
                break;
            case GAME_ENDLESS_WON:
                mGameInfo.setText(R.string.settlement_win);
                btnContinue.setVisibility(View.GONE);
            default:
                break;
        }
        btnContinue.setOnClickListener(this);
        findViewById(R.id.settlement_new_game).setOnClickListener(this);
        findViewById(R.id.settlement_game_share).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.settlement_game_continue:
                MobclickAgent.onEvent(this, "settlement_click_continue");
                setResult(Constants.RESULT_CODE_ENDLESS);
                finish();
                break;
            case R.id.settlement_new_game:
                MobclickAgent.onEvent(this, "settlement_click_new_game");
                setResult(Constants.RESULT_CODE_NEW_GAME);
                finish();
                break;
            case R.id.settlement_game_share:
                MobclickAgent.onEvent(this, "settlement_click_share");
                intent = new Intent(SettlementActivity.this, ShareActivity.class);
                intent.putExtra(IntentConstant.INTENT_SHARE_TYPE, 3);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
