package com.vonnie.game.v2048.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vonnie.game.v2048.R;

import java.util.Objects;

import static com.vonnie.game.v2048.logic.GameController.GAME_LOST;
import static com.vonnie.game.v2048.logic.GameController.GAME_WIN;

/**
 * @author LongpingZou
 * @date 2018/6/23
 */
public class SettlementActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String INTENT_SCORE = "score";
    public static final String INTENT_GAME_STATUS = "status";
    private TextView mGameInfo;
    private TextView mScore;
    private int gameState;
    private Button btnContinue;

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
        gameState = getIntent().getIntExtra(INTENT_GAME_STATUS, 0);
        long score = getIntent().getLongExtra(INTENT_SCORE, 0L);
        mGameInfo = findViewById(R.id.settlement_game_info);
        mScore = findViewById(R.id.settlement_game_score);
        btnContinue = findViewById(R.id.settlement_game_continue);
        mScore.setText(String.valueOf(score));
        switch (gameState) {
            case GAME_WIN:
                mGameInfo.setText(R.string.settlement_win);
                btnContinue.setVisibility(View.VISIBLE);
                break;
            case GAME_LOST:
                mGameInfo.setText(R.string.settlement_game_over);
                btnContinue.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settlement_game_continue:
                break;
            default:
                break;
        }
    }
}
