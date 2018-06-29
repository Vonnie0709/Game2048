package com.vonnie.game.v2048.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class SettlementActivity extends AppCompatActivity implements View.OnClickListener {


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
        long score = getIntent().getLongExtra(IntentConstant.INTENT_SCORE, 0L);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settlement_game_continue:
                setResult(Constants.RESULT_CODE_ENDLESS);
                finish();
                break;
            case R.id.settlement_new_game:
                setResult(Constants.RESULT_CODE_NEW_GAME);
                finish();
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
